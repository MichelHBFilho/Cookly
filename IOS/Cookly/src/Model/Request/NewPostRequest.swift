//
//  NewPostRequest.swift
//  Cookly
//
//  Created by Michel Filho on 09/02/26.
//

import Foundation

struct NewPostRequest : Encodable {
    var recipeName: String
    var description: String
    var prepareTime: Int
    var stepsToPrepare: [String]
}
