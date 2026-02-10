//
//  NewPostView.swift
//  Cookly
//
//  Created by Michel Filho on 09/02/26.
//

import SwiftUI
import PhotosUI

struct NewPostView : View {
    @State private var viewModel = NewPostViewModel()
    
    private let columns = [GridItem(.flexible()), GridItem(.flexible()), GridItem(.flexible())]
    
    private var buttonColor: Color {
        switch(viewModel.requestStatus) {
        case .badRequest: .cooklyRed
        case .nothing: .cooklyBlue
        case .success: .cooklyGreen
        }
    }
    
    var body: some View {
        ScrollView {
            HStack {
                Button {
                    Router.shared.route = .homepage
                } label: {
                    Image(systemName: "arrow.left")
                        .resizable()
                        .scaledToFit()
                        .frame(width: 20)
                        .foregroundStyle(.cooklyLightBlue)
                        .padding()
                }.glassEffect(.regular.interactive().tint(.cooklyBlue))
                Spacer()
                Text("New recipe:")
                    .font(.title)
                    .foregroundStyle(.cooklyGray)
                Spacer()
                
            }
            .padding()
            LazyVStack {
                MyTextField(
                    value: $viewModel.request.recipeName,
                    fieldName: "Recipe Name"
                )
                MyTextField(
                    value: $viewModel.request.description,
                    fieldName: "Description"
                )
                MyTextField(
                    value: Binding(
                        get: {
                            String(viewModel.request.prepareTime)
                        },
                        set: {
                            viewModel.request.prepareTime = Int($0) ?? 0
                        }
                    ),
                    fieldName: "Prepare Time"
                )
                ForEach(viewModel.request.stepsToPrepare.indices, id: \.self) { index in
                    HStack(alignment: .top, spacing: 8) {
                        Text("\(index + 1)")
                            .font(.title3)
                            .fontWeight(.bold)
                            .foregroundColor(.white)
                            .frame(width: 22, height: 22)
                            .background(Circle().fill(.cooklyBlue))
                        
                        Text(viewModel.request.stepsToPrepare[index])
                            .font(.title3)
                            .foregroundStyle(.cooklyGray)
                            .fixedSize(horizontal: false, vertical: true)
                        
                        Spacer()
                        
                        Button {
                            viewModel.deleteStep(at: index)
                        } label: {
                            Image(systemName: "xmark")
                                .resizable()
                                .scaledToFit()
                                .frame(width: 20)
                                .foregroundStyle(.cooklyRed)
                        }
                    }
                    .padding()
                }
                HStack {
                    Text("New Step:")
                        .fontWeight(.semibold)
                        .foregroundStyle(.cooklyBlue)
                    
                    TextField("", text: $viewModel.newStep)
                    
                    Button {
                        viewModel.addStep()
                    } label: {
                        Image(systemName: "plus")
                            .resizable()
                            .scaledToFit()
                            .frame(width: 20)
                    }
                }
                .padding()
                .overlay {
                    RoundedRectangle(cornerRadius: 15)
                        .stroke(lineWidth: 2)
                        .foregroundStyle(.cooklyLightBlue)
                        .shadow(radius: 5)
                }
                .padding()
                
                PhotosPicker("Select images", selection: $viewModel.selectedItems)
                    .onChange(of: viewModel.selectedItems) {
                        Task { await viewModel.updatePhotos() }
                    }
                
                LazyVGrid(columns: columns) {
                    ForEach(viewModel.imagesSwiftUI.indices, id: \.self) { index in
                        viewModel.imagesSwiftUI[index]
                            .resizable()
                            .scaledToFill()
                            .frame(height: 100)
                            .clipped()
                    }
                }
                
                MyButton(
                    title: "Send!",
                    color: buttonColor) {
                        Task { await viewModel.doRequest() }
                    }
            }
        }
    }
}

#Preview {
    NewPostView()
}
