package io.anuke.mindustry.graphics;

import io.anuke.arc.Core;
import io.anuke.arc.graphics.Blending;
import io.anuke.arc.graphics.g2d.Draw;
import io.anuke.arc.graphics.glutils.FrameBuffer;
import io.anuke.arc.util.Disposable;

import static io.anuke.arc.Core.camera;
import static io.anuke.arc.Core.graphics;
import static io.anuke.mindustry.Vars.renderer;

public class Pixelator implements Disposable{
    private FrameBuffer buffer = new FrameBuffer(2, 2);

    public void drawPixelate(){
        graphics.clear(0f, 0f, 0f, 1f);

        float px = Core.camera.position.x, py = Core.camera.position.y;
        Core.camera.position.set((int)px, (int)py + ((int)(camera.height) % 2 == 0 ? 0 : 0.5f));

        int w = (int)(Core.camera.width);
        int h = (int)(Core.camera.height);

        if(!graphics.isHidden() && (buffer.getWidth() != w || buffer.getHeight() != h)){
            buffer.resize(w, h);
        }

        Draw.flush();
        buffer.begin();
        renderer.draw();

        Draw.flush();
        buffer.end();

        Draw.blend(Blending.disabled);
        Draw.rect(Draw.wrap(buffer.getTexture()), Core.camera.position.x, Core.camera.position.y, Core.camera.width, -Core.camera.height);
        Draw.blend();

        Core.camera.position.set(px, py);
    }

    public void rebind(){
        if(enabled()){
            buffer.begin();
        }
    }

    public boolean enabled(){
        return Core.settings.getBool("pixelate");
    }

    @Override
    public void dispose(){
        buffer.dispose();
    }
}