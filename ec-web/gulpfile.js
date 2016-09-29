////////////////////////////////////////////////////////////////////
// Modules
////////////////////////////////////////////////////////////////////
var gulp            = require('gulp'),
    autoprefixer    = require('gulp-autoprefixer'),     //自动添加css前缀
    minifycss       = require('gulp-minify-css'),       //压缩css
    uglify          = require('gulp-uglify'),           //压缩js
    rename          = require('gulp-rename'),           //重命名
    notify          = require('gulp-notify'),           //更改提醒
    rev             = require('gulp-rev'),              //版本控制
    revCollector    = require('gulp-rev-collector'),    //输出到html
    del             = require('del'),                   //清除文件
    sequence        = require("gulp-sequence");

////////////////////////////////////////////////////////////////////
// Configs
////////////////////////////////////////////////////////////////////
var SOURCE_DIR = "src/main/webapp";
var TARGET_DIR = "target/gulp-webapp";

////////////////////////////////////////////////////////////////////
// Tasks
////////////////////////////////////////////////////////////////////

/**
 * Compiles app LESS files, and compress CSS files.
 **/
gulp.task('cssmin', function () {//['clean']
    return gulp.src(SOURCE_DIR+'/css/**/*.css') //该任务针对的文件
        .pipe(minifycss())
        .pipe(rev())
        .pipe(gulp.dest(TARGET_DIR+'/webapp/css'))
        .pipe(rev.manifest())
        .pipe(gulp.dest(TARGET_DIR+'/rev/css'))
        .pipe(notify({message: 'Styles task complete'}));

});

/**
 * Compress JS files.
 **/
gulp.task('jsmin', function () {//,['clean']
    return gulp.src([SOURCE_DIR+'/js/**/*.js', '!'+SOURCE_DIR+'/js/seaconfig.js', '!'+SOURCE_DIR+'/js/lib/**/*.js', '!'+SOURCE_DIR+'/js/plugin/datePicker/**/*.js'])
        .pipe(uglify({
            mangle: {except: ['require', 'exports', 'module', '$', 'window.cas']}//排除混淆关键字
        }))
        .pipe(rev())
        .pipe(gulp.dest(TARGET_DIR+'/webapp/js'))
        .pipe(rev.manifest())
        .pipe(gulp.dest(TARGET_DIR+'/rev/js'))
        .pipe(notify({message: 'Scripts task complete'}));
});

/**
 * Clean folder
 **/
gulp.task('clean', function () {
    //return del([TARGET_DIR+'/webapp', TARGET_DIR+'/rev', TARGET_DIR+'/seaconfig']);
    return del([TARGET_DIR]);
});

/**
 * Replace file name on VM template.
 **/
gulp.task('rev', function () {
    return gulp.src([TARGET_DIR+'/rev/**/*.json', SOURCE_DIR+'/WEB-INF/views/**/*.vm'])
        .pipe(revCollector({
            replaceReved: true
        }))
        .pipe(gulp.dest(TARGET_DIR+'/webapp/WEB-INF/views'))
        .pipe(notify({message: 'Rev task complete'}));
});

/**
 * Replace seaconfig file name.
 */
gulp.task("revj2j", function () {
    return gulp.src([TARGET_DIR+'/rev/js/rev-manifest.json', SOURCE_DIR+'/js/seaconfig.js'])
        .pipe(revCollector({
            replaceReved: true
        }))
        .pipe(gulp.dest(TARGET_DIR+"/seaconfig"))

});

gulp.task("config", function () {
    return gulp.src(TARGET_DIR+"/seaconfig/seaconfig.js")
        .pipe(uglify())
        .pipe(rev())
        .pipe(gulp.dest(TARGET_DIR+'/webapp/js'))
        .pipe(rev.manifest())
        .pipe(gulp.dest(TARGET_DIR+'/rev/seaconfig'))
        .pipe(notify({message: 'config task complete'}));
})

gulp.task('default', function (callback) {
    sequence('clean', 'cssmin', 'jsmin', 'revj2j', 'config', 'rev', callback);
});