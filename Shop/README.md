cliente: (#id_ciente, nombre, email, VIP, fecha_alta )
pedido: (#id_pedido, fecha, importe, pagado,-id_cliente)
envio: (#id_envio, estado, direccion, fecha_entrega, id_pedido) 

Base de datos: 

-- Tabla cliente
CREATE TABLE cliente (
    	id_cliente INT NOT NULL AUTO_INCREMENT,
    	nombre VARCHAR(200) NOT NULL,
    	email VARCHAR(150) NOT NULL,
    	VIP TINYINT(1) DEFAULT 0, 
    	fecha_alta DATE NOT NULL,
    	PRIMARY KEY (id_cliente),
    	UNIQUE (email)
);

-- Tabla pedido
CREATE TABLE pedido (
	id_pedido INT NOT NULL AUTO_INCREMENT,
	fecha DATETIME DEFAULT CURRENT_TIMESTAMP,
	importe DECIMAL(10, 2) NOT NULL,
	pagado TINYINT(1) DEFAULT 0,
    	id_cliente INT NOT NULL,
    	PRIMARY KEY (id_pedido),
    	CONSTRAINT fk_cliente_pedido FOREIGN KEY (id_cliente) 
        	REFERENCES cliente(id_cliente) 
        	ON DELETE CASCADE ON UPDATE CASCADE
);

-- Tabla envio
CREATE TABLE envio (
    	id_envio INT NOT NULL AUTO_INCREMENT,
    	estado VARCHAR(50) NOT NULL,
    	direccion VARCHAR(255) NOT NULL,
    	fecha_entrega DATETIME,
    	id_pedido INT NOT NULL,
    	PRIMARY KEY (id_envio),
    	CONSTRAINT fk_pedido_envio FOREIGN KEY (id_pedido) 
        	REFERENCES pedido(id_pedido) 
        	ON DELETE CASCADE ON UPDATE CASCADE
);
