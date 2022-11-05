package com.lukinhasssss.admin.catalogo.application

abstract class UnitUseCase<IN> {
    abstract fun execute(anIn: IN)
}
