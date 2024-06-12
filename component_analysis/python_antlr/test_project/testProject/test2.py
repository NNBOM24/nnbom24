from transformers import BertTokenizer, BertModel
import tensorflow_hub as hub
import tensorflow as tf
from keras.applications import  AlexNet
from torchvision import models
from vllm import LLM	
import torch
import ctranslate2
import mii




pretrained_model = 'bert-base-chinese'
hub_handle = 'VGG'

tokenizer = BertTokenizer.from_pretrained(model = pretrained_model)
model = BertModel.from_pretrained('bert-base-chinese')



hub_model = hub.load(hub_handle)

torch_hub_model = torch.hub.load('facebookresearch/pytorchvideo', 'slowfast_r50', pretrained=True)

new_model = tf.keras.Sequential([
    hub.KerasLayer("https://tfhub.dev/google/tf2-preview/inception_v3/feature_vector/4", output_shape=[2048], trainable=False),
    tf.keras.layers.Dense(3, activation='softmax')
])

resnet18 = models.resnet18(pretrained=True)

llm = LLM("T5")
llm2 = LLM(model="T5")

translator = ctranslate2.Translator("ende_ctranslate2", device="cpu")

mii_model = "mistralai/Mistral-7B-v0.1"
client = mii.serve(mii_model)