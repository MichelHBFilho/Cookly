//
//  APIError.swift
//  Cookly
//
//  Created by Michel Filho on 01/02/26.
//

import Foundation

enum APIError : String, Error {
    case URLInvalid = "This URL is invalid."
    case Unauthenticated = "You can't access this URL."
    case NotFound = "Can't find this data."
    case ServerError = "There is an error on server-side."
}
