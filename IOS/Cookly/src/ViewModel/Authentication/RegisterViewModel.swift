//
//  RegisterViewModel.swift
//  Cookly
//
//  Created by Michel Filho on 03/02/26.
//

import Foundation
import SwiftUI
import UIKit

@Observable
class RegisterViewModel {
    var form = RegisterRequest()
    var profilePicture: UIImage?
    
    func doRequest() async throws {
        let multipartRequest : MultipartRequest = MultipartRequest(images: {
            var images: [UIImage] = []
            if let profilePicture {
                images.append(profilePicture)
            }
            return images
        }(), data: form)
        
        let boundary = UUID().uuidString
        
        let data = generateMultipartFormDataBody(
            boundary: boundary,
            object: multipartRequest,
            imageFieldName: "profilePicture"
        )
        
        let _:EmptyResponse = try await APIService.shared.multipartRequest(
            endpoint: "authentication/register",
            method: .POST,
            requiresAuth: false,
            body: data,
            boundary: boundary
        )
    }
    
}
