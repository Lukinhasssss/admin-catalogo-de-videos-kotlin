package com.lukinhasssss.admin.catalogo.infrastructure.configuration.usecases

import com.lukinhasssss.admin.catalogo.application.castMember.create.CreateCastMemberUseCase
import com.lukinhasssss.admin.catalogo.application.castMember.create.DefaultCreateCastMemberUseCase
import com.lukinhasssss.admin.catalogo.application.castMember.delete.DefaultDeleteCastMemberUseCase
import com.lukinhasssss.admin.catalogo.application.castMember.delete.DeleteCastMemberUseCase
import com.lukinhasssss.admin.catalogo.application.castMember.retrive.get.DefaultGetCastMemberByIdUseCase
import com.lukinhasssss.admin.catalogo.application.castMember.retrive.get.GetCastMemberByIdUseCase
import com.lukinhasssss.admin.catalogo.application.castMember.retrive.list.DefaultListCastMemberUseCase
import com.lukinhasssss.admin.catalogo.application.castMember.retrive.list.ListCastMemberUseCase
import com.lukinhasssss.admin.catalogo.application.castMember.update.DefaultUpdateCastMemberUseCase
import com.lukinhasssss.admin.catalogo.application.castMember.update.UpdateCastMemberUseCase
import com.lukinhasssss.admin.catalogo.domain.castMember.CastMemberGateway
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class CastMemberUseCaseConfig(
    private val castMemberGateway: CastMemberGateway
) {

    @Bean
    fun createCastMemberUseCase(): CreateCastMemberUseCase {
        return DefaultCreateCastMemberUseCase(
            castMemberGateway = castMemberGateway
        )
    }

    @Bean
    fun updateCastMemberUseCase(): UpdateCastMemberUseCase {
        return DefaultUpdateCastMemberUseCase(
            castMemberGateway = castMemberGateway
        )
    }

    @Bean
    fun getCastMemberByIdUseCase(): GetCastMemberByIdUseCase {
        return DefaultGetCastMemberByIdUseCase(castMemberGateway = castMemberGateway)
    }

    @Bean
    fun listCastMemberUseCase(): ListCastMemberUseCase {
        return DefaultListCastMemberUseCase(castMemberGateway = castMemberGateway)
    }

    @Bean
    fun deleteCastMemberUseCase(): DeleteCastMemberUseCase {
        return DefaultDeleteCastMemberUseCase(castMemberGateway = castMemberGateway)
    }
}
