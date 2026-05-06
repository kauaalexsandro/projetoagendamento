package com.example.exception;

public class AgendaException extends RuntimeException{
    public AgendaException(String mensagem){
        super(mensagem);
    }
}
