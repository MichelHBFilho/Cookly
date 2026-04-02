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
    let author: ProfileResponse
    let description: String
    let createdAt: String
    let isLiked: Bool
    let imagesPaths: [String]
    
    func toPost() -> Post {
        let dateFormatter = ISO8601DateFormatter()
        return Post(
            id: id,
            recipe: recipe.toRecipe(),
            comments: comments.map { response in response.toComment() },
            likes: likes,
            author: author.toProfile(),
            description: description,
            createdAt: dateFormatter.date(from: createdAt) ?? Date(),
            isLiked: isLiked,
            imagePaths: imagesPaths
        )
    }
}

struct RecipeResponse : Decodable {
    let name: String
    let prepareTime: Int
    let stepByStep: [String]
    
    func toRecipe() -> Recipe {
        return Recipe(
            name: name,
            prepareTime: prepareTime,
            stepByStep: stepByStep
        )
    }
}

struct CommentResponse : Decodable {
    let id: String
    let author: ProfileResponse
    let text: String
    let createdAt: String
    
    func toComment() -> Comment {
        let dateFormatter = ISO8601DateFormatter()
        return Comment(
            id: id,
            author: author.toProfile(),
            content: text,
            createdAt: dateFormatter.date(from: createdAt) ?? Date()
        )
    }
}
