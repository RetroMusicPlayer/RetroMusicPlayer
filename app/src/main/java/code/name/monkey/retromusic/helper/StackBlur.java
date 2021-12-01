package code.name.monkey.retromusic.helper;

import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Blur using Java code.
 *
 * <p>This is a compromise between Gaussian Blur and Box blur It creates much better looking blurs
 * than Box Blur, but is 7x faster than my Gaussian Blur implementation.
 *
 * <p>I called it Stack Blur because this describes best how this filter works internally: it
 * creates a kind of moving stack of colors whilst scanning through the image. Thereby it just has
 * to add one new block of color to the right side of the stack and remove the leftmost color. The
 * remaining colors on the topmost layer of the stack are either added on or reduced by one,
 * depending on if they are on the right or on the left side of the stack.
 *
 * @author Enrique López Mañas <eenriquelopez@gmail.com> http://www.neo-tech.es
 *     <p>Author of the original algorithm: Mario Klingemann <mario.quasimondo.com>
 *     <p>Based heavily on http://vitiy.info/Code/stackblur.cpp See
 *     http://vitiy.info/stackblur-algorithm-multi-threaded-blur-for-cpp/
 * @copyright: Enrique López Mañas
 * @license: Apache License 2.0
 */
public class StackBlur {

  static final int EXECUTOR_THREADS = Runtime.getRuntime().availableProcessors();
  static final ExecutorService EXECUTOR = Executors.newFixedThreadPool(EXECUTOR_THREADS);

  private static final short[] stackblur_mul = {
    512, 512, 456, 512, 328, 456, 335, 512, 405, 328, 271, 456, 388, 335, 292, 512,
    454, 405, 364, 328, 298, 271, 496, 456, 420, 388, 360, 335, 312, 292, 273, 512,
    482, 454, 428, 405, 383, 364, 345, 328, 312, 298, 284, 271, 259, 496, 475, 456,
    437, 420, 404, 388, 374, 360, 347, 335, 323, 312, 302, 292, 282, 273, 265, 512,
    497, 482, 468, 454, 441, 428, 417, 405, 394, 383, 373, 364, 354, 345, 337, 328,
    320, 312, 305, 298, 291, 284, 278, 271, 265, 259, 507, 496, 485, 475, 465, 456,
    446, 437, 428, 420, 412, 404, 396, 388, 381, 374, 367, 360, 354, 347, 341, 335,
    329, 323, 318, 312, 307, 302, 297, 292, 287, 282, 278, 273, 269, 265, 261, 512,
    505, 497, 489, 482, 475, 468, 461, 454, 447, 441, 435, 428, 422, 417, 411, 405,
    399, 394, 389, 383, 378, 373, 368, 364, 359, 354, 350, 345, 341, 337, 332, 328,
    324, 320, 316, 312, 309, 305, 301, 298, 294, 291, 287, 284, 281, 278, 274, 271,
    268, 265, 262, 259, 257, 507, 501, 496, 491, 485, 480, 475, 470, 465, 460, 456,
    451, 446, 442, 437, 433, 428, 424, 420, 416, 412, 408, 404, 400, 396, 392, 388,
    385, 381, 377, 374, 370, 367, 363, 360, 357, 354, 350, 347, 344, 341, 338, 335,
    332, 329, 326, 323, 320, 318, 315, 312, 310, 307, 304, 302, 299, 297, 294, 292,
    289, 287, 285, 282, 280, 278, 275, 273, 271, 269, 267, 265, 263, 261, 259
  };

  private static final byte[] stackblur_shr = {
    9, 11, 12, 13, 13, 14, 14, 15, 15, 15, 15, 16, 16, 16, 16, 17,
    17, 17, 17, 17, 17, 17, 18, 18, 18, 18, 18, 18, 18, 18, 18, 19,
    19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 20, 20, 20,
    20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 21,
    21, 21, 21, 21, 21, 21, 21, 21, 21, 21, 21, 21, 21, 21, 21, 21,
    21, 21, 21, 21, 21, 21, 21, 21, 21, 21, 22, 22, 22, 22, 22, 22,
    22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22,
    22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 23,
    23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23,
    23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23,
    23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23,
    23, 23, 23, 23, 23, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24,
    24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24,
    24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24,
    24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24,
    24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24
  };

  public static Bitmap blur(Bitmap original, float radius) {
    int w = original.getWidth();
    int h = original.getHeight();
    int[] currentPixels = new int[w * h];
    original.getPixels(currentPixels, 0, w, 0, 0, w, h);
    int cores = EXECUTOR_THREADS;

    ArrayList<BlurTask> horizontal = new ArrayList<>(cores);
    ArrayList<BlurTask> vertical = new ArrayList<>(cores);
    for (int i = 0; i < cores; i++) {
      horizontal.add(new BlurTask(currentPixels, w, h, (int) radius, cores, i, 1));
      vertical.add(new BlurTask(currentPixels, w, h, (int) radius, cores, i, 2));
    }

    try {
      EXECUTOR.invokeAll(horizontal);
    } catch (InterruptedException e) {
      return null;
    }

    try {
      EXECUTOR.invokeAll(vertical);
    } catch (InterruptedException e) {
      return null;
    }

    return Bitmap.createBitmap(currentPixels, w, h, Bitmap.Config.ARGB_8888);
  }

  private static void blurIteration(
      int[] src, int w, int h, int radius, int cores, int core, int step) {
    int x, y, xp, yp, i;
    int sp;
    int stack_start;
    int stack_i;

    int src_i;
    int dst_i;

    long sum_r, sum_g, sum_b, sum_in_r, sum_in_g, sum_in_b, sum_out_r, sum_out_g, sum_out_b;

    int wm = w - 1;
    int hm = h - 1;
    int div = (radius * 2) + 1;
    int mul_sum = stackblur_mul[radius];
    byte shr_sum = stackblur_shr[radius];
    int[] stack = new int[div];

    if (step == 1) {
      int minY = core * h / cores;
      int maxY = (core + 1) * h / cores;

      for (y = minY; y < maxY; y++) {
        sum_r =
            sum_g = sum_b = sum_in_r = sum_in_g = sum_in_b = sum_out_r = sum_out_g = sum_out_b = 0;

        src_i = w * y; // start of line (0,y)

        for (i = 0; i <= radius; i++) {
          stack_i = i;
          stack[stack_i] = src[src_i];
          sum_r += ((src[src_i] >>> 16) & 0xff) * (i + 1);
          sum_g += ((src[src_i] >>> 8) & 0xff) * (i + 1);
          sum_b += (src[src_i] & 0xff) * (i + 1);
          sum_out_r += ((src[src_i] >>> 16) & 0xff);
          sum_out_g += ((src[src_i] >>> 8) & 0xff);
          sum_out_b += (src[src_i] & 0xff);
        }

        for (i = 1; i <= radius; i++) {
          if (i <= wm) src_i += 1;
          stack_i = i + radius;
          stack[stack_i] = src[src_i];
          sum_r += ((src[src_i] >>> 16) & 0xff) * (radius + 1 - i);
          sum_g += ((src[src_i] >>> 8) & 0xff) * (radius + 1 - i);
          sum_b += (src[src_i] & 0xff) * (radius + 1 - i);
          sum_in_r += ((src[src_i] >>> 16) & 0xff);
          sum_in_g += ((src[src_i] >>> 8) & 0xff);
          sum_in_b += (src[src_i] & 0xff);
        }

        sp = radius;
        xp = radius;
        if (xp > wm) xp = wm;
        src_i = xp + y * w; //   img.pix_ptr(xp, y);
        dst_i = y * w; // img.pix_ptr(0, y);
        for (x = 0; x < w; x++) {
          src[dst_i] =
              (int)
                  ((src[dst_i])
                      | ((((sum_r * mul_sum) >>> shr_sum) & 0xff) << 16)
                      | ((((sum_g * mul_sum) >>> shr_sum) & 0xff) << 8)
                      | ((((sum_b * mul_sum) >>> shr_sum) & 0xff)));
          dst_i += 1;

          sum_r -= sum_out_r;
          sum_g -= sum_out_g;
          sum_b -= sum_out_b;

          stack_start = sp + div - radius;
          if (stack_start >= div) stack_start -= div;
          stack_i = stack_start;

          sum_out_r -= ((stack[stack_i] >>> 16) & 0xff);
          sum_out_g -= ((stack[stack_i] >>> 8) & 0xff);
          sum_out_b -= (stack[stack_i] & 0xff);

          if (xp < wm) {
            src_i += 1;
            ++xp;
          }

          stack[stack_i] = src[src_i];

          sum_in_r += ((src[src_i] >>> 16) & 0xff);
          sum_in_g += ((src[src_i] >>> 8) & 0xff);
          sum_in_b += (src[src_i] & 0xff);
          sum_r += sum_in_r;
          sum_g += sum_in_g;
          sum_b += sum_in_b;

          ++sp;
          if (sp >= div) sp = 0;
          stack_i = sp;

          sum_out_r += ((stack[stack_i] >>> 16) & 0xff);
          sum_out_g += ((stack[stack_i] >>> 8) & 0xff);
          sum_out_b += (stack[stack_i] & 0xff);
          sum_in_r -= ((stack[stack_i] >>> 16) & 0xff);
          sum_in_g -= ((stack[stack_i] >>> 8) & 0xff);
          sum_in_b -= (stack[stack_i] & 0xff);
        }
      }
    }

    // step 2
    else if (step == 2) {
      int minX = core * w / cores;
      int maxX = (core + 1) * w / cores;

      for (x = minX; x < maxX; x++) {
        sum_r =
            sum_g = sum_b = sum_in_r = sum_in_g = sum_in_b = sum_out_r = sum_out_g = sum_out_b = 0;

        src_i = x; // x,0
        for (i = 0; i <= radius; i++) {
          stack_i = i;
          stack[stack_i] = src[src_i];
          sum_r += ((src[src_i] >>> 16) & 0xff) * (i + 1);
          sum_g += ((src[src_i] >>> 8) & 0xff) * (i + 1);
          sum_b += (src[src_i] & 0xff) * (i + 1);
          sum_out_r += ((src[src_i] >>> 16) & 0xff);
          sum_out_g += ((src[src_i] >>> 8) & 0xff);
          sum_out_b += (src[src_i] & 0xff);
        }
        for (i = 1; i <= radius; i++) {
          if (i <= hm) src_i += w; // +stride

          stack_i = i + radius;
          stack[stack_i] = src[src_i];
          sum_r += ((src[src_i] >>> 16) & 0xff) * (radius + 1 - i);
          sum_g += ((src[src_i] >>> 8) & 0xff) * (radius + 1 - i);
          sum_b += (src[src_i] & 0xff) * (radius + 1 - i);
          sum_in_r += ((src[src_i] >>> 16) & 0xff);
          sum_in_g += ((src[src_i] >>> 8) & 0xff);
          sum_in_b += (src[src_i] & 0xff);
        }

        sp = radius;
        yp = radius;
        if (yp > hm) yp = hm;
        src_i = x + yp * w; // img.pix_ptr(x, yp);
        dst_i = x; // img.pix_ptr(x, 0);
        for (y = 0; y < h; y++) {
          src[dst_i] =
              (int)
                  ((src[dst_i])
                      | ((((sum_r * mul_sum) >>> shr_sum) & 0xff) << 16)
                      | ((((sum_g * mul_sum) >>> shr_sum) & 0xff) << 8)
                      | ((((sum_b * mul_sum) >>> shr_sum) & 0xff)));
          dst_i += w;

          sum_r -= sum_out_r;
          sum_g -= sum_out_g;
          sum_b -= sum_out_b;

          stack_start = sp + div - radius;
          if (stack_start >= div) stack_start -= div;
          stack_i = stack_start;

          sum_out_r -= ((stack[stack_i] >>> 16) & 0xff);
          sum_out_g -= ((stack[stack_i] >>> 8) & 0xff);
          sum_out_b -= (stack[stack_i] & 0xff);

          if (yp < hm) {
            src_i += w; // stride
            ++yp;
          }

          stack[stack_i] = src[src_i];

          sum_in_r += ((src[src_i] >>> 16) & 0xff);
          sum_in_g += ((src[src_i] >>> 8) & 0xff);
          sum_in_b += (src[src_i] & 0xff);
          sum_r += sum_in_r;
          sum_g += sum_in_g;
          sum_b += sum_in_b;

          ++sp;
          if (sp >= div) sp = 0;
          stack_i = sp;

          sum_out_r += ((stack[stack_i] >>> 16) & 0xff);
          sum_out_g += ((stack[stack_i] >>> 8) & 0xff);
          sum_out_b += (stack[stack_i] & 0xff);
          sum_in_r -= ((stack[stack_i] >>> 16) & 0xff);
          sum_in_g -= ((stack[stack_i] >>> 8) & 0xff);
          sum_in_b -= (stack[stack_i] & 0xff);
        }
      }
    }
  }

  private static class BlurTask implements Callable<Void> {
    private final int[] _src;
    private final int _w;
    private final int _h;
    private final int _radius;
    private final int _totalCores;
    private final int _coreIndex;
    private final int _round;

    public BlurTask(int[] src, int w, int h, int radius, int totalCores, int coreIndex, int round) {
      _src = src;
      _w = w;
      _h = h;
      _radius = radius;
      _totalCores = totalCores;
      _coreIndex = coreIndex;
      _round = round;
    }

    @Override
    public Void call() throws Exception {
      blurIteration(_src, _w, _h, _radius, _totalCores, _coreIndex, _round);
      return null;
    }
  }
}
