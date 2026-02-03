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
    object: T,
    imageFieldName: String = "imageUploads"
) -> Data {
    var body = Data()
    
    if let data = try? JSONEncoder().encode(object.data),
       let dictionary = try? JSONSerialization.jsonObject(with: data) as? [String: Any] {
        for (key, value) in dictionary {
            body.append("--\(boundary)\r\n")
            body.append("Content-Disposition: form-data; name=\"\(key)\"\r\n")
            body.append("\r\n")
            
            if key == "birthDay", let timestamp = value as? Double {
                let dateFormatter = DateFormatter()
                dateFormatter.dateFormat = "yyyy-MM-dd"
                let date = Date(timeIntervalSince1970: timestamp)
                body.append("\(dateFormatter.string(from: date))\r\n")
            } else {
                body.append("\(value)\r\n")
            }
        }
    }
    
    for image in object.images {
        if let uuid = UUID().uuidString.components(separatedBy: "-").first {
            body.append("--\(boundary)\r\n")
            body.append("Content-Disposition: form-data; name=\"\(imageFieldName)\"; filename=\"\(uuid).jpg\"\r\n")
            body.append("Content-Type: image/jpeg\r\n")
            body.append("\r\n")
            body.append(image.jpegData(compressionQuality: 0.15)!)
            body.append("\r\n")
        }
    }
    
    body.append("--\(boundary)--\r\n")
    
    return body
}
