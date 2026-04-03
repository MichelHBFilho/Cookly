//
//  Post.swift
//  Cookly
//
//  Created by Michel Filho on 05/02/26.
//

import Foundation

struct Post: Identifiable {
    let id: String
    let recipe: Recipe
    var comments: [Comment]
    let likes: Int
    let author: Profile
    let description: String
    let createdAt: Date
    let isLiked: Bool
    let imagePaths: [String]
}

struct Recipe {
    let name : String
    let prepareTime: Int
    let stepByStep: [String]
}

struct Comment: Identifiable {
    let id: String
    let author: Profile
    let content: String
    let createdAt: Date
}
