//
//  LoginRequest.swift
//  Cookly
//
//  Created by Michel Filho on 02/02/26.
//

import Foundation

struct LoginRequest : Encodable {
    let username : String
    let password : String
}
