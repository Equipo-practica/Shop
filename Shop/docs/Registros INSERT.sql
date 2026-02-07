CLIENTE (10 registros)

INSERT INTO cliente (id_cliente, nombre, email, VIP, fecha_alta) VALUES
(1, 'Juan Pérez', 'juan.perez@email.com', TRUE,  '2023-01-15'),
(2, 'María López', 'maria.lopez@email.com', FALSE, '2023-02-10'),
(3, 'Carlos Gómez', 'carlos.gomez@email.com', TRUE,  '2023-03-05'),
(4, 'Laura Martín', 'laura.martin@email.com', FALSE, '2023-03-20'),
(5, 'Pedro Sánchez', 'pedro.sanchez@email.com', FALSE, '2023-04-01'),
(6, 'Ana Torres', 'ana.torres@email.com', TRUE,  '2023-04-18'),
(7, 'David Ruiz', 'david.ruiz@email.com', FALSE, '2023-05-02'),
(8, 'Elena Navarro', 'elena.navarro@email.com', TRUE,  '2023-05-15'),
(9, 'Sergio Molina', 'sergio.molina@email.com', FALSE, '2023-06-01'),
(10,'Lucía Romero', 'lucia.romero@email.com', TRUE,  '2023-06-12');

PEDIDO (10 registros)

INSERT INTO pedido (id_pedido, fecha, importe, pagado, id_cliente) VALUES
(1, '2023-06-20', 120.50, TRUE,  1),
(2, '2023-06-22', 75.99,  FALSE, 2),
(3, '2023-06-25', 220.00, TRUE,  3),
(4, '2023-06-28', 45.30,  TRUE,  4),
(5, '2023-07-01', 89.90,  FALSE, 5),
(6, '2023-07-03', 150.75, TRUE,  6),
(7, '2023-07-05', 60.00,  TRUE,  7),
(8, '2023-07-07', 310.20, FALSE, 8),
(9, '2023-07-09', 99.99,  TRUE,  9),
(10,'2023-07-11', 180.00, TRUE,  10);

ENVÍO (10 registros)

INSERT INTO envio (id_envio, estado, direccion, fecha_entrega, id_pedido) VALUES
(1, 'Entregado', 'Calle Mayor 10, Zaragoza', '2023-06-23', 1),
(2, 'Pendiente', 'Av. Cataluña 45, Barcelona', NULL, 2),
(3, 'Entregado', 'Calle Sol 8, Madrid', '2023-06-28', 3),
(4, 'Entregado', 'Plaza España 3, Valencia', '2023-07-01', 4),
(5, 'Enviado', 'Calle Luna 12, Sevilla', NULL, 5),
(6, 'Entregado', 'Av. Goya 22, Zaragoza', '2023-07-06', 6),
(7, 'Entregado', 'Calle Norte 5, Bilbao', '2023-07-08', 7),
(8, 'Pendiente', 'Calle Sur 18, Málaga', NULL, 8),
(9, 'Entregado', 'Av. Libertad 30, Murcia', '2023-07-12', 9),
(10,'Enviado', 'Calle Río 7, Toledo', NULL, 10);

