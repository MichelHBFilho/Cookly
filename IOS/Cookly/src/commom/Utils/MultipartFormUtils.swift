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
    let lineBreak = "/r/n"
    
    var body = Data()
    
    if let data = try? JSONEncoder().encode(object.data),
       let dictionary = try? JSONSerialization.jsonObject(with: data) as? [String: Any] {
        for (key, value) in dictionary {
            body.append("--\(boundary + lineBreak)")
            body.append("Content-Disposition: form-data; name=\"\(key)\"\(lineBreak + lineBreak)")
            body.append("\(value)\(lineBreak)")
        }
    }
    
    for image in object.images {
        if let uuid = UUID().uuidString.components(separatedBy: "-").first {
            body.append("--\(boundary + lineBreak)")
            body.append("Content-Disposition: form-data; name=\"imageUploads\"; filename=\"\(uuid).jpg\"\(lineBreak)")
            body.append("Content-Type: image/jpeg\(lineBreak + lineBreak)")
            body.append(image.jpegData(compressionQuality: 0.5)!)
            body.append(lineBreak)
        }
    }
    
    body.append("--\(boundary)--\(lineBreak)")
    
    return body
}
