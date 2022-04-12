# ProgJar
Repository ini dibuat untuk memenuhi Tugas Pemrograman Jaringan 2021/2022
Nama:
- Mohammad Faderik Izzul Haq (05111940000023)
- Achmad Akbar Irwanda (05111940000138)

### Tugas 1:
Membuat browser untuk menampilkan link yang dapat diakses dari sebuah url yang diberikan dan sudah mengikuti _redirection_.
### Tugas 2:
Membuat sebuah server yang dapat diakses melalui browser dan menggunakan custom domain dengan mengatur di host (file windows).
### Tugas 3:
Melanjutkan tugas sebelumnya dengan menambahkan _multithread_ untuk menerima beberapa _client_ sekaligus dan berhasil mengunduh dari download manager (seperti IDM) [unfinished]
### Tugas 4:
Membuat platform multichat dimana _client_ dapat mengirim pesan ke semua orang atau pribadi dan melihat siapa saja yang online (terhubung ke server) [ongoing]

### Folder Tree
<details>
  <summary>Lihat</summary>
  Folder diurutkan berdasarkan tugas.


  ```
src 
│
└───browser
│   │   ProgJarWebURI.java
│   │   Response.java
│   │   link.txt
│   
└───server
│   │   ClientThread.java
│   │   Config.java
│   │   Request.java
│   │   Response.java
│   │   WebServer.java
│   │   httpd.conf
│   │
│   └───htdocs
│       │   404.html
│       │   Mirror.png
│       │   file.pdf
│       │   index.html
│       │
│       └───abc
│       │   │   404.html
│       │   │   index.html
│       │
│       └───def
│       │   │   404.html
│       │   │   index.html
│       │
│       └───folder
│           │   app.exe
│           │   file.html
│           │   file.pdf
│
└───chat
    └───client
    │   │   Client.java
    │   │   WorkerThread.java
    │
    └───gui
    │   │   ChatBox.java
    │   │   ClientGUI.java
    │   │   ClientGUI.form
    │
    └───object
    │   │   Chat.java
    │   │   Object.java
    │   │   Person.java
    │
    └───server
        │   Server.java
        │   WorkerThread.java
```

</details>
