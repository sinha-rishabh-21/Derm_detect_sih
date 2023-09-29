import numpy as np
from keras.preprocessing.image import load_img, img_to_array
from keras.models import load_model
from flask import Flask, jsonify, request
import tensorflow as tf

model = load_model("model_final.h5")

labels = {
    0:"Melanocytic nevi",
    1:"Melanoma-Cancerous",
    2:"Benign keratosis-like lesions",
    3:"Basal cell carcinoma-Cancerous",
    4:"Actinic keratoses",
    5:"Vascular lesions",
    6:"Dermatofibroma"
}

def prepare_image(img_path):
    img = load_img(img_path, target_size=(224,224,3))
    img = img_to_array(img)
    img = img/255
    img = np.expand_dims(img, [0])
    answer = model.predict(img)
    y_class = answer.argmax(axis=-1)
    print(y_class)
    y = " ".join(str(x) for x in y_class)
    y = int(y)
    res = labels[y]
    print(res)
    return res.capitalize()


app = Flask(__name__)
@app.route('/predict', methods=['POST'])
def infer_image():
    if 'file' not in request.files:
        return jsonify(error="Please try again. The Image doesn't exist")

    file = request.files.get('file')
    img_bytes = file.read()
    img_path = "./upload_images/test.jpg"
    with open(img_path, "wb") as img:
        img.write(img_bytes)
    result = prepare_image(img_path)
    return jsonify(prediction=result)

if __name__ == '__main__':
    app.run(debug=False, host='0.0.0.0')