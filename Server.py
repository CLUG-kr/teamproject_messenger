import pyscreenshot
import shutil  # 추가
import os
import sqlite3
from flask import Flask, request, send_from_directory, url_for, redirect, abort, jsonify
import flask
import random
from io import BytesIO
import json
import datetime
from PIL import Image
import socket
from werkzeug import secure_filename
import image

IP = str(socket.gethostbyname(socket.gethostname()))


"""
Requirements
    pyscreenshot
    flask
    pillow
"""


UPLOAD_FOLDER = '/path/to/the/uploads'
ALLOWED_EXTENSIONS = set(['png', 'pdf', 'txt', 'jpg', 'jpeg', 'gif', 'mp4', 'avi', 'PNG', 'PDF', 'TXT', 'JPG', 'JPEG', 'GIF', 'MP4', 'AVI'])

if not os.path.exists(UPLOAD_FOLDER):
    os.makedirs(UPLOAD_FOLDER)

OPENED_SERVER = {}
for dic in os.listdir(UPLOAD_FOLDER):
    OPENED_SERVER[dic] = UPLOAD_FOLDER + '/' + dic

app = Flask(__name__)
app.config['UPLOAD_FOLDER'] = UPLOAD_FOLDER


def allowed_file(filename):
    return '.' in filename and \
           filename.rsplit('.', 1)[1] in ALLOWED_EXTENSIONS


@app.route('/')
def main():
    return "index"

@app.route('/login', methods=['POST']) #Clientname_password 형식
def login():
    req = request.get_json(force=True)
    username = req['username']
    password = req['password']
    conn = sqlite3.connect(UPLOAD_FOLDER + '/userManage.db')
    curs = conn.cursor()
    curs.execute("SELECT * FROM userManage")
    Access = False
    for i in curs.fetchall():
        if i[0] == username:
            if i[1] == password:
                Access = True
                name = i[2]
    conn.close()

    if Access == True:
        data = {}
        data['OX']='O'
        return json.dumps(data, ensure_ascii=False)
    else:
        data = {}
        data['OX'] ='X'
        return json.dumps(data, ensure_ascii=False)

@app.route('/register', methods=['POST']) #username_password_ClientName 형식
def register():
    try:
        conn = sqlite3.connect(UPLOAD_FOLDER + '/userManage.db')
        curs = conn.cursor()
        curs.execute('create table userManage (username, password, name)')
        conn.commit()
        conn.close()
    except:
        pass
    try:
        os.makedirs(UPLOAD_FOLDER + '/user')
    except:
        pass
    try:
        req = request.get_json(force=True)
        username = req['username']
        password = req['password']
        name = req['name']

        conn = sqlite3.connect(UPLOAD_FOLDER + '/userManage.db')
        curs = conn.cursor()
        curs.execute("SELECT * FROM userManage")
        Access = True
        for i in curs.fetchall():
            if i[0] == username:
                Access = False
                break
        conn.close()
        if(Access == False):
            raise ZeroDivisionError #go to line 125 exept

        conn = sqlite3.connect(UPLOAD_FOLDER + '/userManage.db')
        curs = conn.cursor()
        curs.execute("insert into userManage values ('" + username + "', '" + password + "', '" + name + "')")
        conn.commit()
        conn.close()

        conn = sqlite3.connect(UPLOAD_FOLDER + '/user/' + username + '.db')
        curs = conn.cursor()
        try:
            curs.execute('create table server (serverCode, serverName)')
            conn.commit()
            curs.execute('create table friend (friend)')
            conn.commit()
        except:
            pass
        data = {}
        data["OX"] = 'O'
        return json.dumps(data, ensure_ascii=False)
    except:
        data = {}
        data["OX"] = 'X'
        return json.dumps(data, ensure_ascii=False)


@app.route('/addFriend', methods =['POST'])
def addFriend():
    req = request.get_json(force=True)
    username = req['username']
    friendName = req['friendName']
    conn = sqlite3.connect(UPLOAD_FOLDER + '/user/' + username +'.db')
    curs = conn.cursor()
    curs.execute("insert into chat values('" + friendName + "')")
    conn.commit()
    conn.close()
    conn = sqlite3.connect(UPLOAD_FOLDER + '/user/' + friendName +'.db')
    curs = conn.cursor()
    curs.execute("insert into chat values('" + username + "')")
    conn.commit()
    conn.close()

@app.route('/showFriend', methods =['POST'])
def showFriend():
    req = request.get_json(force=True)
    data = {}
    data['fiendName'] = []
    username = req['username']
    conn = sqlite3.connect(UPLOAD_FOLDER + '/user/' + username +'.db')
    curs = conn.cursor()
    curs.execute("SELECT * FROM userManage")
    for i in curs.fetchall():
        data['fiendName'].append(str(i))

    return json.dumps(data, ensure_ascii=False)

@app.route('/message', methods =['POST'])
def message():
    req = request.get_json(force=True)
    print(req)
    serverCode = req['serverCode']
    conn = sqlite3.connect(OPENED_SERVER[serverCode] + '/chat.db')
    curs = conn.cursor()
    try:
        username = req['username']
        message = req['message']
        print(message)
        curs.execute("insert into chat values('" + username + "', '" + message + "')")
        conn.commit()
    except:
        pass

    curs.execute("SELECT * FROM chat")
    try:
        data = {}
        dat = curs.fetchall()[-1]
        dat = dat[0] + '`' + dat[1]
        data['O'] = dat
    except:
        data['O'] = ''

    jsonstring = json.dumps(data, ensure_ascii=False)
    return jsonstring


@app.route('/downloads', methods = ['POST'])
def get_file():
    req = request.get_json(force=True)
    serverCode = req['serverCode']
    fileName = req['fileName']

    return send_from_directory(UPLOAD_FOLDER + '/' + serverCode, fileName)

"""
@app.route('/uploads')
def upload_file():
    if request.method == 'POST':

        if 'file' not in request.files:
            return redirect(request.url)

        file = request.files['file']

        if file.filename == '':
            return redirect(request.url)


        if file and allowed_file(file.filename):
            filename = secure_filename(file.filename)
            file.save(os.path.join(app.config['UPLOAD_FOLDER'], filename))
            print(app.config['UPLOAD_FOLDER'])
            return redirect(url_for('upload_file', filename=filename))
    return '''
    <!doctype html>
    <title>Upload new File</title>
    <h1>Upload new File</h1>
    <form action="" method=post enctype=multipart/form-data>
      <p><input type=file name=file>
         <input type=submit value=Upload>
    </form>
    '''
"""
@app.route('/uploads')
def uploads():
    req = request.get_json(force=True)
    imageName = req['imageName']
    imageString= req['imageData']
    serverName = req['serverName']

    import base64
    imgData = base64.b64decode(imageString)
    filename = imageName  # I assume you have a way of picking unique filenames
    with open(filename, 'wb') as f:
        f.write(imgData)


@app.route('/getServer', methods = ['POST'])
def getServer():
    req = request.get_json(force=True)
    username = req['username']

    conn = sqlite3.connect(UPLOAD_FOLDER + '/user/' + username + '.db')
    curs = conn.cursor()
    try:
        curs.execute('create table server (serverCode, serverName)')
        conn.commit()
        curs.execute('create table friend (friend)')
        conn.commit()
    except:
        pass

    curs.execute("SELECT * FROM server")
    data = {}
    data["serverName"] = []
    data["serverCode"] = []
    for i in curs.fetchall():
        print(i)
        data["serverCode"].append(i[0])
        data["serverName"].append(i[1])
    jsonstring = json.dumps(data, ensure_ascii=False)
    print(jsonstring)
    return jsonstring

@app.route('/chatServer', methods=['POST'])
def chatServer():
    data = {}
    req = request.get_json(force=True)
    username = req["username"]

    try:
        serverName = req["serverName"]
        if serverName in list(OPENED_SERVER.keys()):
            data["serverCode"] = None
            data["serverName"] = None
            return
        serverCode = str(datetime.datetime.now().microsecond)
    except:
        serverCode = req["serverCode"]
        conn = sqlite3.connect(UPLOAD_FOLDER + '/chatServer.db')
        curs = conn.cursor()
        curs.execute("SELECT * FROM chatServer")
        for i in curs.fetchall():
            if serverCode == i[0]:
                serverName = i[1]
                break
        conn.close()

    numOfPeople = 1

    conn = sqlite3.connect(UPLOAD_FOLDER + '/chatServer.db')
    curs = conn.cursor()
    try:
        curs.execute('create table chatServer (serverCode, serverName)')
        conn.commit()
        conn.close()
    except:
        pass

    if serverCode not in list(OPENED_SERVER.keys()):
        serverCode = str(datetime.datetime.now().microsecond)
        conn = sqlite3.connect(UPLOAD_FOLDER + '/chatServer.db')
        curs = conn.cursor()
        curs.execute("insert into chatServer values('" + serverCode + "', '" + serverName + "')")
        conn.commit()
        conn.close()
        OPENED_SERVER[serverCode] = UPLOAD_FOLDER + '/' + serverCode  # 서버폴더 생성
        os.makedirs(OPENED_SERVER[serverCode])
        conn = sqlite3.connect(OPENED_SERVER[serverCode] + '/serverInfo.db')
        curs = conn.cursor()
        curs.execute('create table serverInfo (username)')
        conn.commit()
        conn.close()

    conn = sqlite3.connect(OPENED_SERVER[serverCode] + '/serverInfo.db')
    curs = conn.cursor()
    curs.execute("SELECT * FROM serverInfo")
    check = False

    data["username"] = []
    data["username"].append(username)
    for i in curs.fetchall():
        if username == str(i)[2:-3]: #??왜 이런 오류가 발생하는지 모름
            check = True
        else:
            data["username"].append(str(i)[2:-3])
            numOfPeople += 1
    conn.close()
    if check == False:
        conn = sqlite3.connect(OPENED_SERVER[serverCode] + '/serverInfo.db')
        curs = conn.cursor()
        curs.execute("insert into serverInfo values ('" + username + "')")
        conn.commit()
        conn.close()
        conn = sqlite3.connect(UPLOAD_FOLDER + '/user/' + username + '.db')
        curs = conn.cursor()
        curs.execute("insert into server values ('" + serverCode + "', '" + serverName + "')")
        conn.commit()
        conn.close()

    conn = sqlite3.connect(OPENED_SERVER[serverCode] + '/chat.db')
    curs = conn.cursor()
    try:
        curs.execute('create table chat (username, message)')
        conn.commit()
    except:
        pass
    conn.close()

    data["serverCode"] = serverCode
    data["serverName"] = serverName
    data["numOfPeople"] = str(numOfPeople)

    return json.dumps(data, ensure_ascii=False)


@app.route('/closeChatServer', methods=['POST']) #   "DELETE from Orders where Order_Status = \"Expired\""
def closeChatServer():
    req = request.get_json(force=True)
    serverCode = req['serverCode']


    if os.path.exists(OPENED_SERVER[serverCode]):
        shutil.rmtree(OPENED_SERVER[serverCode])
    OPENED_SERVER.pop(serverCode)
    conn = sqlite3.connect(UPLOAD_FOLDER + '/chatServer.db')
    curs = conn.cursor()
    curs.execute("DELETE from chatServer where serverCode = '" + serverCode + "'")
    conn.commit()
    conn.close()

    data = []
    for i in os.listdir(UPLOAD_FOLDER + "/user"):
        data.append(i[:-3])

    for i in data:
        conn = sqlite3.connect(UPLOAD_FOLDER + '/user/' + i + '.db')
        curs = conn.cursor()
        curs.execute("DELETE from server where serverCode = '" + serverCode + "'")
        conn.commit()
        conn.close()


    jsonstring = json.dumps(serverCode+"_X", ensure_ascii=False)
    return jsonstring


@app.route('/showFile', methods=['POST'])
def showFile():
    req = request.get_json(force=True)
    serverName = req['serverName']
    data = os.listdir(UPLOAD_FOLDER + '/' + serverName)[:-1] #db파일을 제외시키기 위한 인덱승
    return json.dumps(data, ensure_ascii=False)

@app.route('/screen.png')
def serve_pil_image():
    img_buffer = BytesIO()
    pyscreenshot.grab().save(img_buffer, 'PNG', quality=50)
    img_buffer.seek(0)
    return flask.send_file(img_buffer, mimetype='image/png')


@app.route('/js/<path:path>')
def send_js(path):
    return flask.send_from_directory('js', path)


@app.route('/screenShare')
def serve_img():
    return flask.render_template('screen.html')


if __name__ == '__main__':
    app.run(host=IP, debug=True, port=12345)
