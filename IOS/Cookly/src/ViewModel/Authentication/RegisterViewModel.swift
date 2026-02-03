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
    
    enum RequestStatus {
        case success
        case badRequest
        case nothing
    }
    
    var requestStatus: RequestStatus = .nothing
    
    func doRequest() async throws {
        
        guard form.lastName != "" &&
                form.name != "" &&
                form.password != "" &&
                form.username != "" else {
            requestStatus = .badRequest
            try await Task.sleep(for: .seconds(1))
            requestStatus = .nothing
            return
        }
        
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
        
        let (_, statusCode) = try await APIService.shared.multipartRequest(
            endpoint: "authentication/register",
            method: .POST,
            requiresAuth: false,
            body: data,
            boundary: boundary
        ) as (EmptyResponse, Int)
        
        if((200...299).contains(statusCode)) {
            requestStatus = .success
        }
    }
    
}
