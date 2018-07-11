import pyscreenshot
import shutil  # 추가
import os
import sqlite3
from flask import Flask, request, send_from_directory, url_for, redirect, abort, jsonify
import flask
import random
import json
from io import BytesIO

from User import User
import Room


from werkzeug import secure_filename

"""
Requirements
    pyscreenshot
    flask
    pillow
"""


UPLOAD_FOLDER = '/path/to/the/uploads'
ALLOWED_EXTENSIONS = set(['png', 'pdf', 'txt', 'jpg', 'jpeg', 'gif', 'mp4', 'avi', 'PNG'])

if not os.path.exists(UPLOAD_FOLDER):
    os.makedirs(UPLOAD_FOLDER)


app = Flask(__name__)
app.config['UPLOAD_FOLDER'] = UPLOAD_FOLDER


def allowed_file(filename):
    return '.' in filename and \
           filename.rsplit('.', 1)[1] in ALLOWED_EXTENSIONS


@app.route('/', methods = ['GET'])
def main():
    if request.method == 'GET':
        return 'Index Page';


@app.route('/register', methods=['POST', 'GET'])
def register():
    if request.method == 'POST':

        """
       Example Curl Request (on Windows)
       curl -X POST 127.0.0.1:12345/register --header "content-type:application/json"  -d "{\"id\":\" \", \"pw\":\" \"}"

       Example Curl Request (on Linux)
       curl -X POST 127.0.0.1:12345/register --header "content-type:application/json"  -d "{"id" : " ", "pw" : " "}"
        """
        forms = request.get_json()
        user_id = forms['id']
        user_pw = forms['pw']
        user_nickname = forms['nickname']
        user_access_token = User(user_id).get_user_token()

        conn = sqlite3.connect('test.db')
        cur = conn.cursor()
        sql = "insert into User(ID, PASSWORD, ACCESS_TOKEN, NICKNAME) VALUES (?, ?, ?, ?);"

        cur.execute(sql, (user_id, user_pw, user_access_token, user_nickname))
        conn.commit()
        conn.close()
        return "Register Succeed"
    elif request.method == 'GET':

        return 'Register Page, Using Post Method'


@app.route('/login', methods=['POST', 'GET'])
def login():
    if request.method == 'GET':
        return "Login Page, Using Post Method."
    elif request.method == 'POST':
        """
        Example Curl Request (on Windows)
        curl -X POST 127.0.0.1:12345/login --header "content-type:application/json"  -d "{\"id\":\" \", \"pw\": \" \"}"

        Example Curl Request (on Linux)
        curl -X POST 127.0.0.1:12345/login --header "content-type:application/json"  -d "{"id" : " ", "pw" : " "
        }"
        """
        forms = request.get_json()
        user_id = forms['id']
        user_pw = forms['pw']
        cur = sqlite3.connect('test.db').cursor();
        cur.execute("select * from User where ID='" + user_id + "' and  PASSWORD='" + user_pw + "'")
        res = cur.fetchall()
        if len(res) == 0:
            return 'LoginFailed'
        for i in cur.fetchall():
            print(i)

        rq = User(user_id)
        jsn = {}
        jsn['user_access_token'] = rq.get_user_token()
        jsn['user_nickname'] =rq.get_user_nickname()
        return json.dumps(jsn, ensure_ascii=False, indent=4, sort_keys=True)


@app.route('/user/<username>')
def req_create_room():
    if request.method == 'POST':
        req = request.get_json()
        req['user_access_token']
        req['room_name']

@app.route('/send_message', methods=['POST'])
def get_message():
    if request.method == 'POST':
        req = request.get_json()
        req['message']
        req['user_access_token']
        req['room_num']
        return 'send_ok'


@app.route('/downloads/<path:path>')
def get_file(path):
    return send_from_directory(UPLOAD_FOLDER, path, as_attachment=True)


@app.route('/uploads', methods=['GET', 'POST'])
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
    app.run(host='127.0.0.1', debug=True, port=12345)
"""
import pyscreenshot
import os
import sqlite3
from flask import Flask, request, send_from_directory, url_for, redirect, abort, jsonify
import flask
from io import BytesIO

from werkzeug import secure_filename


UPLOAD_FOLDER = '/path/to/the/uploads'
ALLOWED_EXTENSIONS = set(['png', 'pdf', 'txt', 'jpg', 'jpeg', 'gif', 'mp4', 'avi', 'PNG', 'PDF', 'TXT', 'JPG', 'JPEG', 'GIF', 'MP4', 'AVI']) #수정

OPENED_SERVER = {}       #추가
for dic in os.listdir(UPLOAD_FOLDER):       #추가
    OPENED_SERVER[dic] = UPLOAD_FOLDER + '/' + dic       #추가


if not os.path.exists(UPLOAD_FOLDER):
    os.makedirs(UPLOAD_FOLDER)


app = Flask(__name__)
app.config['UPLOAD_FOLDER'] = UPLOAD_FOLDER

def allowed_file(filename):
    return '.' in filename and \
           filename.rsplit('.', 1)[1] in ALLOWED_EXTENSIONS

@app.route('/', methods = ['GET'])
def main():
    if request.method == 'GET':
        return 'Index Page';


@app.route('/register', methods=['POST', 'GET'])
def register():
    if request.method == 'POST':
        forms = request.get_json()
        user_id = forms['id']
        user_pw = forms['pw']
        cur = sqlite3.connect('test.db').cursor();
        cur.execute("insert into User (ID, PASSWORD) VALUES ('" + user_id + "', '" + user_pw + "')")
        res = cur.fetchall()

        if len(res) == 0:
            return 'Register Failed'
        for i in cur.fetchall():
            print(i)

        return "Register Succeed"
    elif request.method == 'GET':

        return 'Register Page, Using Post Method'


@app.route('/login', methods=['POST', 'GET'])
def login():
    if request.method == 'GET':
        return "Login Page, Using Post Method."
    elif request.method == 'POST':
        forms = request.get_json()
        user_id = forms['id']
        user_pw = forms['pw']
        cur = sqlite3.connect('test.db').cursor();
        cur.execute("select * from User where id='" + user_id + "' and  PASSWORD='" + user_pw + "'")
        res = cur.fetchall()
        if len(res) == 0:
            return 'Login Failed, POST Method'
        for i in cur.fetchall():
            print(i)
        return 'Login Succeed, POST Method'

@app.route('/user/<username>')
def profile(username):
    # show the user profile for that user
    return 'User %s' % username

@app.route('/post/<int:post_id>')
def show_post(post_id):
    # show the post with the given id, the id is an integer
    return 'Post %d' % post_id

@app.route('/downloads/<path:path>')  #체팅방이름/파일이름 입력
def get_file(path):
    return send_from_directory(UPLOAD_FOLDER, path, as_attachment=True)


@app.route('/uploads', methods=['GET', 'POST'])
def upload_file():
    if request.method == 'POST':

        server_name = request #채팅방 이름만추가해주면 될거야
        if 'file' not in request.files:
            return redirect(request.url)

        file = request.files['file']

        if file.filename == '':
            return redirect(request.url)


        if file and allowed_file(file.filename):
            filename = secure_filename(file.filename)
            print(filename)
            file.save(os.path.join(app.config['UPLOAD_FOLDER'] + '/' + 'hi', filename))
            return redirect(url_for('upload_file', filename=filename))
    return '''
    <!doctype html>
    <title>Upload new File</title>
    <h1>Upload new File</h1>
    <form action="" method=post enctype=multipart/form-data>
      <p><input type=file name=file>
         <input type=submit value=Upload>
      </p>
      <td> <input type='text' name='server_name' size=10> </td> 
    </form>
    ''' #서버이름추가

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

@app.route('/makeChatServer/<server_name>')
def makeChatServer(server_name): # 방을 만든 사람아이디 받기
    OPENED_SERVER[server_name] = UPLOAD_FOLDER + '/' + server_name # 서버폴더 생성

    if not os.path.exists(OPENED_SERVER[server_name]):
        os.makedirs(OPENED_SERVER[server_name])

    return OPENED_SERVER[server_name]

@app.route('/closeChatServer/<server_name>')
def closeCharServer(server_name):
    if os.path.exists(OPENED_SERVER[server_name]):
        shutil.rmtree(OPENED_SERVER[server_name])
    OPENED_SERVER.pop(server_name)
    return server_name + " is closed"

@app.route('/showOpenedServer')
def showOpenedServer():
    print(OPENED_SERVER)
    return "Show"

@app.route('/showCurrentFile/<server_name>')
def showCurrentFile(server_name):
    for file in os.listdir(OPENED_SERVER[server_name]):
        print(file, end=' ')
        print('')
    return "Show"

if __name__ == '__main__':
    app.run(host='127.0.0.1', debug=True, port=12345)
    """