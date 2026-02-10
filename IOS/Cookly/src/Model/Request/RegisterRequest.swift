//
//  RegisterRequest.swift
//  Cookly
//
//  Created by Michel Filho on 03/02/26.
//

import Foundation

struct RegisterRequest : Encodable {
    var username: String = ""
    var password: String = ""
    var name: String = ""
    var lastName: String = ""
    var birthDay: Date = Date()
}
