//
//  Post.swift
//  Cookly
//
//  Created by Michel Filho on 05/02/26.
//

import Foundation

struct Post {
    let id: String
    let recipe: Recipe
    let comments: [Comment]
    let likes: Int
    let author: String
    let description: String
    let createdAt: Date
    let imagePaths: [String]
}

struct Recipe {
    let name : String
    let prepareTime: Int
    let stepByStep: [String]
}

struct Comment {
    let id: String
    let author: String
    let content: String
    let createdAt: Date
}
