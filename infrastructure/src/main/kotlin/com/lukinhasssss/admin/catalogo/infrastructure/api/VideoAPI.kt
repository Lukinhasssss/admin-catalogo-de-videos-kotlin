package com.lukinhasssss.admin.catalogo.infrastructure.api

import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.RequestMapping

@RequestMapping(value = ["videos"])
@Tag(name = "Video")
interface VideoAPI
