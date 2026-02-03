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
    var data: Encodable { get }
}

struct MultipartRequest : MultipartRequestProtocol {
    var images: [UIImage]
    var data: Encodable
}
