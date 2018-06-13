
#  Нашою структурою двних є sqlalchemy база даних
#  В ній ми зберігаємо замовлення клієнтів
#  Тут ми її створюємо

from flask import Flask
from flask_sqlalchemy import SQLAlchemy
from config import Config

app = Flask(__name__)
app.config.from_object(Config)
db = SQLAlchemy(app)


