//点击效果博客页面点击就可以看到
    !function(e, t, a) {
        function n() {
            c(".heart{width: 40px;height: 40px;position: fixed;color: red;}"), o(), r()
        }

        function r() {
            for(var e = 0; e < d.length; e++) d[e].alpha <= 0 ? (t.body.removeChild(d[e].el), d.splice(e, 1)) : (d[e].y--, d[e].scale += .004, d[e].alpha -= .013, d[e].el.style.cssText = "left:" + d[e].x + "px;top:" + d[e].y + "px;opacity:" + d[e].alpha + ";transform:scale(" + d[e].scale + "," + d[e].scale + ") rotate(0deg);color:" + d[e].color + ";z-index:99999");
            requestAnimationFrame(r)
        }

        function o() {
            var t = "function" == typeof e.onclick && e.onclick;
            e.onclick = function(e) {
                t && t(), i(e)
            }
        }

        function i(e) {
            var a = t.createElement("div");
            var arr = ["富强","民主","文明","和谐","自由","平等","公正","法治","爱国","敬业","诚信","友善"];
            var i = Math.floor(Math.random()*12);

            a.innerHTML = arr[i];
            a.className = "heart", d.push({
                el: a,
                x: e.clientX - 5,
                y: e.clientY - 5,
                scale: 1,
                alpha: 1,
                color: s()
            }), t.body.appendChild(a)
        }

        function c(e) {
            var a = t.createElement("style");
            a.type = "text/css";
            try {
                a.appendChild(t.createTextNode(e))
            } catch(t) {
                a.styleSheet.cssText = e
            }
            t.getElementsByTagName("head")[0].appendChild(a)
        }

        function s() {
            return "rgb(" + ~~(255 * Math.random()) + "," + ~~(255 * Math.random()) + "," + ~~(255 * Math.random()) + ")"
        }
        var d = [];
        e.requestAnimationFrame = function() {
            return e.requestAnimationFrame || e.webkitRequestAnimationFrame || e.mozRequestAnimationFrame || e.oRequestAnimationFrame || e.msRequestAnimationFrame || function(e) {
                setTimeout(e, 1e3 / 60)
            }
        }(), n()
    }(window, document);