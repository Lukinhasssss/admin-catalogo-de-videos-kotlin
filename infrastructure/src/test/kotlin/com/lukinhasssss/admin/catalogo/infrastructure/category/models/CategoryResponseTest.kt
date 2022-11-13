package com.lukinhasssss.admin.catalogo.infrastructure.category.models

import com.lukinhasssss.admin.catalogo.JacksonTest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.json.JacksonTester

@JacksonTest
class CategoryResponseTest {

    @Autowired
    private lateinit var json: JacksonTester<CategoryResponse>
}
