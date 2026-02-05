//
//  ProfileResponse.swift
//  Cookly
//
//  Created by Michel Filho on 05/02/26.
//

struct ProfileResponse : Decodable {
    let profilePictureURL: String
    let birthDay: String
    let fullName: String
    let username: String
}
