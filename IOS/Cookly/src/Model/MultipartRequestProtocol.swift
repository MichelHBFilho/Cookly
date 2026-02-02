//
//  MultipartRequest.swift
//  Cookly
//
//  Created by Michel Filho on 02/02/26.
//

import Foundation
import UIKit

protocol MultipartRequestProtocol: Encodable {
    var images: [UIImage] { get }
    var data: Encodable { get }
}
