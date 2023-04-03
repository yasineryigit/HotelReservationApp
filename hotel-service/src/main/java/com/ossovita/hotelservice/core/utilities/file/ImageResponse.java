package com.ossovita.hotelservice.core.utilities.file;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ImageResponse {

    String fileName;

    String imageUrl;
}
