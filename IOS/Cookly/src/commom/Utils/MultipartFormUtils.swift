//
//  MultipartFormUtils.swift
//  Cookly
//
//  Created by Michel Filho on 02/02/26.
//
import Foundation
import UIKit
func generateMultipartFormDataBody<T: MultipartRequestProtocol>(
    boundary : String,
    object: T
) -> Data {
    var body = Data()
    
    if let data = try? JSONEncoder().encode(object.data),
       let dictionary = try? JSONSerialization.jsonObject(with: data) as? [String: Any] {
        for (key, value) in dictionary {
            body.append("\(boundary)\r\n")
            body.append("Content-Disposition: form-data; name=\"\(key)\"\r\n\r\n")
            body.append("\(value)\r\n")
        }
    }
    
    for image in object.images {
        if let uuid = UUID().uuidString.components(separatedBy: "-").first {
            body.append("--\(boundary)\r\n")
            body.append("Content-Disposition: form-data; name=\"imageUploads\"; filename=\"\(uuid).jpg\"\r\n")
            body.append("Content-Type: image/jpeg\r\n\r\n")
            body.append(image.jpegData(compressionQuality: 0.5)!)
            body.append("\r\n")
        }
    }
    
    body.append("--\(boundary)--\r\n")
    
    print(String(data:body, encoding: .utf8) ?? "can't")
    
    return body
}
