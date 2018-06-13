# Створення абстрактного типу даних, який дозволяє записувати замовлення


from app import db

class Order(db.Model):
    __tablename__ = "orders"
    id = db.Column(db.Integer, primary_key=True)
    wish_list = db.Column(db.String(64))
    orderer = db.Column(db.String(64))
    lat = db.Column(db.Integer)
    lng = db.Column(db.Integer)

    def __repr__(self):
        return "<Order %r> " % self.id
