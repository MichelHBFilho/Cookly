//
//  RegisterForm.swift
//  Cookly
//
//  Created by Michel Filho on 02/02/26.
//

import UIKit

struct RegisterForm: Encodable {
    var username: String = ""
    var password: String = ""
    var name: String = ""
    var lastName: String = ""
    var birthDay: Date = Date()
}

struct RegisterRequest: MultipartRequestProtocol {
    var data: any Encodable
    
    var images: [UIImage]
    
    var data: RegisterForm
}
