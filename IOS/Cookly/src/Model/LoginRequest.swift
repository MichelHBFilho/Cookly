//
//  LoginRequest.swift
//  Cookly
//
//  Created by Michel Filho on 02/02/26.
//

import Foundation

struct LoginRequest : Encodable {
    var username : String = ""
    var password : String = ""
}
