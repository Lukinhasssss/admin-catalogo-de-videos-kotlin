package com.lukinhasssss.admin.catalogo.application.castMember.retrive.list

import com.lukinhasssss.admin.catalogo.application.UseCase
import com.lukinhasssss.admin.catalogo.domain.pagination.Pagination
import com.lukinhasssss.admin.catalogo.domain.pagination.SearchQuery

abstract class ListCastMemberUseCase : UseCase<SearchQuery, Pagination<CastMemberListOutput>>()
