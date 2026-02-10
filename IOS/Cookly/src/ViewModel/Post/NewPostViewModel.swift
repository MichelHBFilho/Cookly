//
//  NewPostViewModel.swift
//  Cookly
//
//  Created by Michel Filho on 09/02/26.
//

import Foundation
import UIKit
import PhotosUI
import SwiftUI
@Observable
class NewPostViewModel {
    var request: NewPostRequest = NewPostRequest(
        recipeName: "",
        description: "",
        prepareTime: 0,
        stepsToPrepare: []
    )
    var newStep: String = ""
    var images: [UIImage] = []
    var selectedItems: [PhotosPickerItem] = []
    var imagesSwiftUI: [Image] = []
    var requestStatus: RequestStatus = .nothing
    
    func addStep() {
        guard newStep != "" else { return }
        request.stepsToPrepare.append(newStep)
        newStep = ""
    }
    
    func deleteStep(at index: Int) {
        request.stepsToPrepare.remove(at: index)
    }
    
    func updatePhotos() async {
        images.removeAll()
        
        for item in selectedItems {
            if let image = try? await item.loadTransferable(type: Data.self) {
                images.append(UIImage(data: image) ?? UIImage())
            }
        }
        
        for image in images {
            imagesSwiftUI.append(Image(uiImage: image))
        }
    }
    
    func doRequest() async {
        do {
            let jsonForm = try JSONEncoder().encode(request)
            let multipartRequest = MultipartRequest(
                images: images,
                data: .json(jsonForm)
            )
            
            let boundary = UUID().uuidString
            
            let data = generateMultipartFormDataBody(
                boundary: boundary,
                object: multipartRequest,
                imageFieldName: "images"
            )
            
            let (_, statusCode) = try await APIService.shared.multipartRequest(
                endpoint: "post/new",
                method: .POST,
                requiresAuth: true,
                body: data,
                boundary: boundary,
            ) as (EmptyResponse?, Int)
            
            if((200...299).contains(statusCode)) {
                requestStatus = .success
                Router.shared.route = .homepage
            }
        } catch let error as EncodingError {
            requestStatus = .badRequest
        } catch {
            requestStatus = .badRequest
            ErrorManager.shared.handle(error)
            print(error)
        }
    }
}
