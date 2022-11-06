package com.lukinhasssss.admin.catalogo.application

abstract class UseCase<IN, OUT> {
    abstract fun execute(anIn: IN): OUT
}
