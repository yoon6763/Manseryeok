package com.example.manseryeok.utils.notionAPI.responseDTO

data class InquiryRequestDTO(
    val children: List<Children>,
    val parent: Parent,
    val properties: Properties
)