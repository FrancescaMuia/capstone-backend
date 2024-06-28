package it.epicode.viniEVinili;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

public class CloudinaryTest {
    public static void main(String[] args) {
        // Hardcode the URL to test
        String cloudinaryUrl = "cloudinary://846621127847989:vuzuNHYGcFbuTj_QTCTd-9ajoKM@dtfpc1ez0";
        Cloudinary cloudinary = new Cloudinary(cloudinaryUrl);

        try {
            // Perform a simple operation to test the connection
            var result = cloudinary.uploader().upload("C:\\Users\\Francesca\\Desktop\\main-qimg-2da9aa7c805c0d2c8955ffbfdf6d647a-lq.jpg", ObjectUtils.emptyMap());
            System.out.println("Upload successful: " + result.get("url"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

