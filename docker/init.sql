-- =============================================================================
-- SCRIPT DE INICIALIZAÇÃO COMPLETO - TODO LEARNING
-- Inclui: Usuários, Categorias, Tags, Tarefas, Subtarefas, Anexos, Auditoria e Notificações
-- =============================================================================

CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- 1. Tabela de Usuários (Configurações de Contato e Preferências de Alerta)
CREATE TABLE tb_users (
                          id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                          name VARCHAR(100) NOT NULL,
                          email VARCHAR(150) NOT NULL UNIQUE,
                          phone_number VARCHAR(20) NOT NULL, -- WhatsApp / SMS
                          notify_by_sms BOOLEAN DEFAULT TRUE,
                          notify_by_email BOOLEAN DEFAULT TRUE,
                          created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
                          updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- 2. Tabela de Categorias / Projetos
CREATE TABLE tb_categories (
                               id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                               user_id UUID NOT NULL,
                               name VARCHAR(50) NOT NULL,
                               color_hex VARCHAR(7) DEFAULT '#000000',
                               created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
                               CONSTRAINT fk_category_user FOREIGN KEY (user_id) REFERENCES tb_users(id) ON DELETE CASCADE
);

-- 3. Tabela de Tags / Etiquetas (Ex: #backend, #urgente, #estudo)
CREATE TABLE tb_tags (
                         id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                         user_id UUID NOT NULL,
                         name VARCHAR(30) NOT NULL,
                         color_hex VARCHAR(7) DEFAULT '#808080',
                         created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
                         CONSTRAINT fk_tag_user FOREIGN KEY (user_id) REFERENCES tb_users(id) ON DELETE CASCADE,
                         CONSTRAINT uq_user_tag_name UNIQUE (user_id, name)
);

-- 4. Tabela Principal de Tarefas
CREATE TABLE tb_tasks (
                          id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                          user_id UUID NOT NULL,
                          category_id UUID,
                          title VARCHAR(150) NOT NULL,
                          description TEXT,
                          status VARCHAR(20) NOT NULL DEFAULT 'PENDING',
                          priority VARCHAR(10) NOT NULL DEFAULT 'MEDIUM',
                          deadline TIMESTAMP WITH TIME ZONE NOT NULL,
                          overdue_notified BOOLEAN DEFAULT FALSE, -- Flag para controle do RabbitMQ Worker
                          created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
                          updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
                          CONSTRAINT fk_task_user FOREIGN KEY (user_id) REFERENCES tb_users(id) ON DELETE CASCADE,
                          CONSTRAINT fk_task_category FOREIGN KEY (category_id) REFERENCES tb_categories(id) ON DELETE SET NULL,
                          CONSTRAINT chk_status CHECK (status IN ('PENDING', 'IN_PROGRESS', 'COMPLETED', 'CANCELLED')),
                          CONSTRAINT chk_priority CHECK (priority IN ('LOW', 'MEDIUM', 'HIGH', 'URGENT'))
);

-- 5. Tabela de Relacionamento N:N entre Tarefas e Tags
CREATE TABLE tb_task_tags (
                              task_id UUID NOT NULL,
                              tag_id UUID NOT NULL,
                              PRIMARY KEY (task_id, tag_id),
                              CONSTRAINT fk_tasktag_task FOREIGN KEY (task_id) REFERENCES tb_tasks(id) ON DELETE CASCADE,
                              CONSTRAINT fk_tasktag_tag FOREIGN KEY (tag_id) REFERENCES tb_tags(id) ON DELETE CASCADE
);

-- 6. Tabela de Subtarefas (Checklist)
CREATE TABLE tb_subtasks (
                             id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                             task_id UUID NOT NULL,
                             title VARCHAR(200) NOT NULL,
                             is_completed BOOLEAN DEFAULT FALSE,
                             created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
                             CONSTRAINT fk_subtask_task FOREIGN KEY (task_id) REFERENCES tb_tasks(id) ON DELETE CASCADE
);

-- 7. Tabela de Anexos (Arquivos / Documentos armazenados no S3 ou MinIO)
CREATE TABLE tb_task_attachments (
                                     id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                                     task_id UUID NOT NULL,
                                     file_name VARCHAR(255) NOT NULL,
                                     file_size_bytes BIGINT NOT NULL,
                                     content_type VARCHAR(100) NOT NULL, -- ex: application/pdf, image/png
                                     storage_key VARCHAR(500) NOT NULL, -- Chave de referência no S3/MinIO
                                     uploaded_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
                                     CONSTRAINT fk_attachment_task FOREIGN KEY (task_id) REFERENCES tb_tasks(id) ON DELETE CASCADE
);

-- 8. Tabela de Auditoria e Histórico de Alterações (Audit Logs)
CREATE TABLE tb_task_audits (
                                id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                                task_id UUID NOT NULL,
                                user_id UUID NOT NULL,
                                field_changed VARCHAR(50) NOT NULL, -- ex: STATUS, PRIORITY, DEADLINE
                                old_value TEXT,
                                new_value TEXT,
                                changed_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
                                CONSTRAINT fk_audit_task FOREIGN KEY (task_id) REFERENCES tb_tasks(id) ON DELETE CASCADE,
                                CONSTRAINT fk_audit_user FOREIGN KEY (user_id) REFERENCES tb_users(id) ON DELETE CASCADE
);

-- 9. Tabela de Log de Notificações Enviadas (Mensagens via RabbitMQ / Twilio)
CREATE TABLE tb_notification_logs (
                                      id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                                      user_id UUID NOT NULL,
                                      task_id UUID NOT NULL,
                                      channel VARCHAR(20) NOT NULL, -- WHATSAPP, SMS, EMAIL
                                      destination VARCHAR(150) NOT NULL,
                                      status VARCHAR(20) NOT NULL, -- SENT, FAILED
                                      message_body TEXT,
                                      sent_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
                                      CONSTRAINT fk_log_user FOREIGN KEY (user_id) REFERENCES tb_users(id) ON DELETE CASCADE,
                                      CONSTRAINT fk_log_task FOREIGN KEY (task_id) REFERENCES tb_tasks(id) ON DELETE CASCADE
);

-- =============================================================================
-- ÍNDICES DE PERFORMANCE (Para consultas rápidas com Spring Data JPA)
-- =============================================================================
CREATE INDEX idx_tasks_user_id ON tb_tasks(user_id);
CREATE INDEX idx_tasks_status ON tb_tasks(status);
CREATE INDEX idx_tasks_deadline ON tb_tasks(deadline);
CREATE INDEX idx_tasks_overdue_check ON tb_tasks(status, overdue_notified, deadline);
CREATE INDEX idx_audits_task_id ON tb_task_audits(task_id);
CREATE INDEX idx_notification_logs_task_id ON tb_notification_logs(task_id);

-- =============================================================================
-- DADOS DE TESTE INICIAIS (DML)
-- =============================================================================

-- Usuário de Teste
INSERT INTO tb_users (id, name, email, phone_number) VALUES
    ('11111111-1111-1111-1111-111111111111', 'Matheus Silva', 'matheus@email.com', '+5511999998888');

-- Categoria
INSERT INTO tb_categories (id, user_id, name, color_hex) VALUES
    ('22222222-2222-2222-2222-222222222222', '11111111-1111-1111-1111-111111111111', 'Estudos Spring Boot', '#FF5733');

-- Tags
INSERT INTO tb_tags (id, user_id, name, color_hex) VALUES
                                                       ('aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa', '11111111-1111-1111-1111-111111111111', 'backend', '#0000FF'),
                                                       ('bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb', '11111111-1111-1111-1111-111111111111', 'urgente', '#FF0000');

-- Tarefa (Exemplo de tarefa já vencida para testar o envio de alerta pelo RabbitMQ)
INSERT INTO tb_tasks (id, user_id, category_id, title, description, status, priority, deadline) VALUES
    ('33333333-3333-3333-3333-333333333333',
     '11111111-1111-1111-1111-111111111111',
     '22222222-2222-2222-2222-222222222222',
     'Criar Worker do RabbitMQ',
     'Desenvolver o serviço consumidor que envia mensagens de WhatsApp ao passar o prazo.',
     'PENDING',
     'URGENT',
     NOW() - INTERVAL '1 hour');

-- Associação Tarefa <-> Tags
INSERT INTO tb_task_tags (task_id, tag_id) VALUES
                                               ('33333333-3333-3333-3333-333333333333', 'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa'),
                                               ('33333333-3333-3333-3333-333333333333', 'bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb');

-- Subtarefas (Checklist)
INSERT INTO tb_subtasks (task_id, title, is_completed) VALUES
                                                           ('33333333-3333-3333-3333-333333333333', 'Instalar biblioteca Spring AMQP', TRUE),
                                                           ('33333333-3333-3333-3333-333333333333', 'Criar classe de configuração de Exchange e Queue', FALSE);

-- Exemplo de Registro de Auditoria (Histórico de alteração de status)
INSERT INTO tb_task_audits (task_id, user_id, field_changed, old_value, new_value) VALUES
    ('33333333-3333-3333-3333-333333333333', '11111111-1111-1111-1111-111111111111', 'STATUS', 'IN_PROGRESS', 'PENDING');