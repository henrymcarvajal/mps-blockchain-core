// SPDX-License-Identifier: MIT
pragma solidity >=0.4.22 <0.8.0;

contract Compraventa {

    enum Estado {
        CONTRATO_CREADO,
        VENDEDOR_ENVIO_FONDOS,
        COMPRADOR_ENVIO_FONDOS,
        FONDOS_LIBERADOS
    }

    uint constant FACTOR_DEPOSITO_VENDEDOR = 1;
    uint constant FACTOR_DEPOSITO_COMPRADOR = 1;

    Estado public estado;
    uint public precio;
    uint public depositoVendedor;
    uint public depositoComprador;

    address public comprador;
    address public vendedor;

    event CambioEstado();

    constructor(uint _precio) public {
        precio = _precio;
        depositoVendedor = _precio * FACTOR_DEPOSITO_VENDEDOR;
        depositoComprador = _precio * FACTOR_DEPOSITO_COMPRADOR;
        estado = Estado.CONTRATO_CREADO;
    }

    modifier estadoValido(Estado _estado) {
        require(estado == _estado, "Estado incorrecto");
        _;
    }

    modifier fondosCorrectos(uint _fondos) {
        require(msg.value == _fondos, "Fondos incorrectos");
        _;
    }

    function enviarFondosVendedor() public payable 
    estadoValido(Estado.CONTRATO_CREADO)
    fondosCorrectos(depositoVendedor) {
        vendedor = msg.sender;
        estado = Estado.VENDEDOR_ENVIO_FONDOS;
        emit CambioEstado();
    }

    function enviarFondosComprador() public payable 
    estadoValido(Estado.VENDEDOR_ENVIO_FONDOS) 
    fondosCorrectos(depositoComprador + precio) {
        comprador = msg.sender;
        estado = Estado.COMPRADOR_ENVIO_FONDOS;
        emit CambioEstado();
    }

    function liberarFondos() public
    estadoValido(Estado.COMPRADOR_ENVIO_FONDOS) {        
        require(msg.sender == comprador, "Emisor incorrecto");
        estado = Estado.FONDOS_LIBERADOS;
        vendedor.call.value(depositoVendedor + precio)("");
        comprador.call.value(depositoComprador)("");
        emit CambioEstado();
    }
}