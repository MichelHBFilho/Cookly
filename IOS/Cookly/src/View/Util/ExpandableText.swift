//
//  ExpandableText.swift
//  Cookly
//
//  Created by Michel Filho on 05/02/26.
//

import SwiftUI

struct ExpandableText: View {
    let text : String
    @State private var expanded: Bool = false
    var body: some View {
        if(text.count < 120) {
            Text(text)
        } else {
            Text("\(expanded ? text : String(text.prefix(120))) ... \(expanded ? "See less" : "See more")")
                .transition(.move(edge: .top))
                .onTapGesture {
                    withAnimation(.easeInOut(duration: 0.2)) {
                        expanded.toggle()
                    }
                }
        }
    }
}

#Preview {
    ExpandableText(text: """
                   Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.
                   """)
}
