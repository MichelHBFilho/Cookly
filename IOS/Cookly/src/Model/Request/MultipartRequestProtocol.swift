//
//  MultipartRequest.swift
//  Cookly
//
//  Created by Michel Filho on 02/02/26.
//

import Foundation
import UIKit

protocol MultipartRequestProtocol {
    var images: [UIImage] { get }
    var data: MultipartData { get }
}

struct MultipartRequest : MultipartRequestProtocol {
    var images: [UIImage]
    var data: MultipartData
}

enum MultipartData {
    case model(Encodable)
    case json(Data)
}
