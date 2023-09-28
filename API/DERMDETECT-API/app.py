from flask import request, Flask
from tensorflow.keras.model import load_model
from tensorflow.keras.applications.vgg16 import preprocess_input
import numpy as np
from PIL import Image

app = Flask(__name__)

@app.route('/disease_prediction')
class ImageUpload(MethodView):
    model = load_model('your_model.h5')
    path

    #for preprocessing the image according to our data model
    def imagePreProcess(self, path):
        
        img = Image.open([path])
        img = img.resize(())  #input size of model
        img = np.array(img)
        img = np.expand_dims(img, axis=0)  #batch dimension
        img = preprocess_input(img)
        return img 
    
    #for getting image from user
    def image_check(self, path):
        image_file = Image.open(path)
        allowed = ('.png', '.jpeg', '.jpg', '.webp')
        if image_file.format in allowed:
            image = imagePreProcess(path)
        else:
            return {'message':'file format not supported'}

    def image_upload(self):
        image = request.files('image')
