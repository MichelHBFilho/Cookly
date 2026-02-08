//
//  PostResponse.swift
//  Cookly
//
//  Created by Michel Filho on 07/02/26.
//

import Foundation

struct PostResponse : Decodable {
    let id : String
    let recipe: RecipeResponse
    let comments: [CommentResponse]
    let likes: Int
    let author: String
    let description: String
    let createdAt: String
    let imagesPaths: [String]
}

struct RecipeResponse : Decodable {
    let name: String
    let prepareTime: Int
    let stepByStep: [String]
}

struct CommentResponse : Decodable {
    let id: String
    let author: String
    let text: String
    let createdAt: String
}
