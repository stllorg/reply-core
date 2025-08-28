INSERT INTO users (username, email, password)
VALUES
    ('admin', 'adm@testmail.com', '$2y$12$7WxeGPCKKc/w5ZrR4I/YjeoJ0p2AyjbjCsYt/Y6ygHo9phOWd0ZsO'),
    ('manager', 'manager@email.com', '$2y$12$7WxeGPCKKc/w5ZrR4I/YjeoJ0p2AyjbjCsYt/Y6ygHo9phOWd0ZsO'),
    ('support', 'support@email.com', '$2y$12$7WxeGPCKKc/w5ZrR4I/YjeoJ0p2AyjbjCsYt/Y6ygHo9phOWd0ZsO'),
    ('newuser', 'newuser@email.com', '$2y$12$7WxeGPCKKc/w5ZrR4I/YjeoJ0p2AyjbjCsYt/Y6ygHo9phOWd0ZsO');

INSERT INTO roles (name)
VALUES
    ('admin'),
    ('manager'),
    ('support'),
    ('user');

INSERT INTO user_roles (user_id, role_id)
VALUES
    ((SELECT id FROM users WHERE username = 'admin'), 1),
    ((SELECT id FROM users WHERE username = 'manager'), 2),
    ((SELECT id FROM users WHERE username = 'support'), 3),
    ((SELECT id FROM users WHERE username = 'newuser'), 4);

INSERT INTO tickets (subject, status, is_repeat, user_id)
VALUES
    ('Login', 'in_progress', FALSE, (SELECT id FROM users WHERE username = 'newuser')),
    ('Atualizar email', 'open', FALSE, (SELECT id FROM users WHERE username = 'newuser')),
    ('Verificar email', 'open', FALSE, (SELECT id FROM users WHERE username = 'newuser'));

-- Insere as mensagens de exemplo para os tickets.
-- Nota: 'user_id' aqui se refere a quem enviou a mensagem.
INSERT INTO ticket_messages (ticket_id, message, user_id)
VALUES
    ((SELECT id FROM tickets WHERE subject = 'Login'), 'N?o consigo fazer login.', (SELECT id FROM users WHERE username = 'newuser')),
    ((SELECT id FROM tickets WHERE subject = 'Login'), 'Ol?! Meu nome ? John Doe, especialista em suporte. Voc? poderia tentar usar a op??o redefinir sua senha?', (SELECT id FROM users WHERE username = 'support')),
    ((SELECT id FROM tickets WHERE subject = 'Login'), 'J? tentei, mas apareceu um erro dizendo que meu email n?o existe, embora meu email j? existe.', (SELECT id FROM users WHERE username = 'newuser')),
    ((SELECT id FROM tickets WHERE subject = 'Atualizar email'), 'Quero atualizar meu email.', (SELECT id FROM users WHERE username = 'newuser')),
    ((SELECT id FROM tickets WHERE subject = 'Verificar email'), 'N?o consigo verificar o meu email.', (SELECT id FROM users WHERE username = 'newuser'));