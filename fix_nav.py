import re, os

os.chdir('/Users/clemente/ProyectoGameShop/gameShop/src/main/resources/templates')

nav = (
    '            <nav>\n'
    '                <a href="/">Inicio</a>\n'
    '                <a href="/catalogo/disponibles">Disponibles</a>\n'
    '                <a href="/catalogo/populares">Populares</a>\n'
    '                <a href="/carrito">\U0001f6d2 Carrito</a>\n'
    '                <span th:if="${session.usuarioNombre != null}" style="color:#00d4ff;">\U0001f464 '
    '<span th:text="${session.usuarioNombre}"></span></span>\n'
    '                <a th:if="${session.usuarioId != null}" href="/pedidos">Mis Pedidos</a>\n'
    "                <a th:if=\"${session.usuarioRol == 'ADMIN'}\" href=\"/admin\">Admin</a>\n"
    '                <a th:if="${session.usuarioId != null}" href="/logout" style="color:#ff3333;">Cerrar Sesi\u00f3n</a>\n'
    '                <a th:unless="${session.usuarioId != null}" href="/login">Login</a>\n'
    '            </nav>'
)

pat = re.compile(
    r'<nav>\s*'
    r'<a href="/">Inicio</a>\s*'
    r'<a href="/catalogo/disponibles">Disponibles</a>\s*'
    r'<a href="/catalogo/populares">Populares</a>\s*'
    r'<a href="/carrito">[^<]*Carrito</a>\s*'
    r'</nav>',
    re.DOTALL
)

files = [
    'catalogo/disponibles.html',
    'catalogo/plataforma.html',
    'catalogo/populares.html',
    'catalogo/resultados-busqueda.html',
    'producto/ficha.html',
]

for f in files:
    if os.path.exists(f):
        c = open(f, encoding='utf-8').read()
        n = pat.sub(nav, c)
        if n != c:
            open(f, 'w', encoding='utf-8').write(n)
            print('OK:', f)
        else:
            print('SKIP (no match):', f)
    else:
        print('NOT FOUND:', f)

