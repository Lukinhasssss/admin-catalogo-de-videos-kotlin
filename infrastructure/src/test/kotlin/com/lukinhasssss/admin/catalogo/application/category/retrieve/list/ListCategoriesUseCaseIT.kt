package com.lukinhasssss.admin.catalogo.application.category.retrieve.list

import com.lukinhasssss.admin.catalogo.IntegrationTest
import com.lukinhasssss.admin.catalogo.domain.category.Category
import com.lukinhasssss.admin.catalogo.domain.pagination.SearchQuery
import com.lukinhasssss.admin.catalogo.infrastructure.category.persistence.CategoryJpaEntity
import com.lukinhasssss.admin.catalogo.infrastructure.category.persistence.CategoryRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.springframework.beans.factory.annotation.Autowired

@IntegrationTest
class ListCategoriesUseCaseIT {

    @Autowired
    private lateinit var useCase: ListCategoriesUseCase

    @Autowired
    private lateinit var categoryRepository: CategoryRepository

    @BeforeEach
    fun mockUp() {
        val categories = listOf(
            Category.newCategory("Filmes", null, true),
            Category.newCategory("Netflix Originals", "Títulos de autoria da Netflix", true),
            Category.newCategory("Amazon Originals", "Títulos de autoria da Amazon Prime", true),
            Category.newCategory("Documentários", null, true),
            Category.newCategory("Animes", "Os melhores animes shonen", true),
            Category.newCategory("Kids", "Categoria para crianças", true),
            Category.newCategory("Series", null, true)
        ).map(CategoryJpaEntity::from)

        categoryRepository.saveAllAndFlush(categories)
    }

    @Test
    fun givenAValidTerm_whenTermDoesntMatchsPrePresisted_shouldReturnEmptyPage() {
        val expectedPage = 0
        val expectedPerPage = 10
        val expectedTerms = "sdfhb asdhjb"
        val expectedSort = "name"
        val expectedDirection = "asc"
        val expectedItemsCount = 0
        val expectedTotal = 0L

        val aQuery = SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection)

        val actualResult = useCase.execute(aQuery)

        assertEquals(expectedItemsCount, actualResult.items.size)
        assertEquals(expectedPage, actualResult.currentPage)
        assertEquals(expectedPerPage, actualResult.perPage)
        assertEquals(expectedTotal, actualResult.total)
    }

    @ParameterizedTest
    @CsvSource(
        "fil, 0, 10, 1, 1, Filmes",
        "net, 0, 10, 1, 1, Netflix Originals",
        "ZON, 0, 10, 1, 1, Amazon Originals",
        "DOc, 0, 10, 1, 1, Documentários",
        "aNi, 0, 10, 1, 1, Animes",
        "crianças, 0, 10, 1, 1, Kids",
        "ser, 0, 10, 1, 1, Series",
        "da Amazon Prime, 0, 10, 1, 1, Amazon Originals"
    )
    fun givenAValidTerm_whenCallsListCategories_shouldReturnCategoriesFiltered(
        expectedTerms: String,
        expectedPage: Int,
        expectedPerPage: Int,
        expectedItemsCount: Int,
        expectedTotal: Long,
        expectedCategoryName: String
    ) {
        val expectedSort = "name"
        val expectedDirection = "asc"

        val aQuery = SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection)

        val actualResult = useCase.execute(aQuery)

        assertEquals(expectedItemsCount, actualResult.items.size)
        assertEquals(expectedPage, actualResult.currentPage)
        assertEquals(expectedPerPage, actualResult.perPage)
        assertEquals(expectedTotal, actualResult.total)
        assertEquals(expectedCategoryName, actualResult.items.first().name)
    }

    @ParameterizedTest
    @CsvSource(
        "name, asc, 0, 10, 7, 7, Amazon Originals",
        "name, desc, 0, 10, 7, 7, Series",
        "createdAt, asc, 0, 10, 7, 7, Filmes",
        "createdAt, desc, 0, 10, 7, 7, Series"
    )
    fun givenAValidSortAndDirection_whenCallsListCategories_thenShouldReturnCategoriesOrdered(
        expectedSort: String,
        expectedDirection: String,
        expectedPage: Int,
        expectedPerPage: Int,
        expectedItemsCount: Int,
        expectedTotal: Long,
        expectedCategoryName: String
    ) {
        val expectedTerms = ""

        val aQuery = SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection)

        val actualResult = useCase.execute(aQuery)

        assertEquals(expectedPage, actualResult.currentPage)
        assertEquals(expectedPerPage, actualResult.perPage)
        assertEquals(expectedItemsCount, actualResult.items.size)
        assertEquals(expectedTotal, actualResult.total)
        assertEquals(expectedCategoryName, actualResult.items.first().name)
    }

    @ParameterizedTest
    @CsvSource(
        "0, 2, 2, 7, Amazon Originals;Animes",
        "1, 2, 2, 7, Documentários;Filmes",
        "2, 2, 2, 7, Kids;Netflix Originals",
        "3, 2, 1, 7, Series"
    )
    fun givenAValidPage_whenCallsListCategories_shouldReturnCategoriesPaginated(
        expectedPage: Int,
        expectedPerPage: Int,
        expectedItemsCount: Int,
        expectedTotal: Long,
        expectedCategoriesName: String
    ) {
        val expectedSort = "name"
        val expectedDirection = "asc"
        val expectedTerms = ""

        val aQuery = SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection)

        val actualResult = useCase.execute(aQuery)

        assertEquals(expectedPage, actualResult.currentPage)
        assertEquals(expectedPerPage, actualResult.perPage)
        assertEquals(expectedItemsCount, actualResult.items.size)
        assertEquals(expectedTotal, actualResult.total)

        for ((index, expectedCategoryName) in expectedCategoriesName.split(";").withIndex()) {
            val actualCategoryName = actualResult.items[index].name
            assertEquals(expectedCategoryName, actualCategoryName)
        }

        // for ((index, expectedCategoryName) in expectedCategoriesName.split(";".toRegex()).dropLastWhile { it.isEmpty() }
        //     .toTypedArray().withIndex()) {
        //     val actualCategoryName: Unit = actualResult.items().get(index).name()
        //     assertEquals(expectedCategoryName, actualCategoryName)
        // }
    }
}
