import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.zip.ZipFile;

public class Icon {
	static BufferedImage tex(ZipFile z, String p) throws Exception {
		return ImageIO.read(z.getInputStream(z.getEntry(p)));
	}

	public static void main(String[] a) throws Exception {
		ZipFile z = new ZipFile(a[0]);
		BufferedImage sword = tex(z, "assets/minecraft/textures/item/netherite_sword.png");
		BufferedImage glint = tex(z, "assets/minecraft/textures/misc/enchanted_glint_item.png");
		BufferedImage font = tex(z, "assets/minecraft/textures/font/ascii.png");

		final int SIZE = 512, P = 24;          // P = screen pixels per Minecraft pixel
		final int SLOT = 18 * P;               // vanilla slot is 18x18 with a 16x16 item inside
		final int SX = (SIZE - SLOT) / 2, SY = SX;
		final int IX = SX + P, IY = SY + P;    // item origin inside the slot

		BufferedImage out = new BufferedImage(SIZE, SIZE, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = out.createGraphics();
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		// Rounded dark tile behind everything (reads well on Modrinth's light and dark themes)
		g.setPaint(new GradientPaint(0, 0, new Color(0x3A2F4A), 0, SIZE, new Color(0x16121C)));
		g.fill(new RoundRectangle2D.Float(0, 0, SIZE, SIZE, 96, 96));
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);

		// Vanilla inventory slot: dark top/left bevel, white bottom/right bevel, grey fill
		g.setColor(new Color(0x373737)); g.fillRect(SX, SY, SLOT - P, SLOT - P);
		g.setColor(Color.WHITE);         g.fillRect(SX + P, SY + P, SLOT - P, SLOT - P);
		g.setColor(new Color(0x8B8B8B)); g.fillRect(SX + P, SY + P, SLOT - 2 * P, SLOT - 2 * P);
		g.setColor(new Color(0x8B8B8B)); g.fillRect(SX, SY + SLOT - P, P, P);
		g.setColor(new Color(0x8B8B8B)); g.fillRect(SX + SLOT - P, SY, P, P);
		g.dispose();

		// Sword, pixel-scaled, with an additive purple enchantment glint over opaque pixels
		double ang = Math.toRadians(-10), cos = Math.cos(ang), sin = Math.sin(ang);
		int gw = glint.getWidth(), gh = glint.getHeight();
		for (int y = 0; y < 16 * P; y++) {
			for (int x = 0; x < 16 * P; x++) {
				int c = sword.getRGB(x / P, y / P);
				if ((c >>> 24) < 128) continue;
				double u = (x * cos - y * sin) / (16.0 * P) * 0.35 + 0.30;
				double v = (x * sin + y * cos) / (16.0 * P) * 0.35 + 0.10;
				int gc = glint.getRGB(Math.floorMod((int) (u * gw), gw), Math.floorMod((int) (v * gh), gh));
				int gr0 = (gc >> 16) & 255, gg0 = (gc >> 8) & 255, gb0 = gc & 255;
				double lum = (0.3 * gr0 + 0.59 * gg0 + 0.11 * gb0) / 255.0;
				// Only the bright streaks of the glint show, like the in-game shimmer over dark netherite
				double t = Math.min(1, Math.max(0, (lum - 0.07) / 0.43)); double ga = 0.30 + 1.25 * t * t;
				int r = (c >> 16) & 255, gr = (c >> 8) & 255, b = c & 255;
				int gr2 = (gc >> 16) & 255, gg2 = (gc >> 8) & 255, gb2 = gc & 255;
				r = Math.min(255, (int) (r + gr2 * ga));
				gr = Math.min(255, (int) (gr + gg2 * ga));
				b = Math.min(255, (int) (b + gb2 * ga));
				out.setRGB(IX + x, IY + y, 0xFF000000 | (r << 16) | (gr << 8) | b);
			}
		}

		// "II" in the vanilla font at the item's top-left, white with the standard 1px shadow
		String text = "II";
		int cx = 0;
		int[][] glyphs = new int[text.length()][];
		for (int pass = 0; pass < 2; pass++) {
			cx = 0;
			for (int i = 0; i < text.length(); i++) {
				char ch = text.charAt(i);
				int gx0 = (ch % 16) * 8, gy0 = (ch / 16) * 8, width = 0;
				for (int gy = 0; gy < 8; gy++) for (int gx = 0; gx < 8; gx++)
					if ((font.getRGB(gx0 + gx, gy0 + gy) >>> 24) > 0) width = Math.max(width, gx + 1);
				int off = pass == 0 ? 1 : 0;                 // shadow first, then white text
				int col = pass == 0 ? 0xFF3F3F3F : 0xFFFFFFFF;
				for (int gy = 0; gy < 8; gy++) for (int gx = 0; gx < 8; gx++) {
					if ((font.getRGB(gx0 + gx, gy0 + gy) >>> 24) == 0) continue;
					int px = IX + (cx + gx + off) * P, py = IY + (gy + off) * P;
					for (int dy = 0; dy < P; dy++) for (int dx = 0; dx < P; dx++) out.setRGB(px + dx, py + dy, col);
				}
				cx += width + 1;
			}
		}

		ImageIO.write(out, "png", new File(a[1]));
		// Small preview at Modrinth's list size
		BufferedImage small = new BufferedImage(96, 96, BufferedImage.TYPE_INT_ARGB);
		Graphics2D sg = small.createGraphics();
		sg.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		sg.drawImage(out, 0, 0, 96, 96, null); sg.dispose();
		ImageIO.write(small, "png", new File(a[2]));
	}
}
