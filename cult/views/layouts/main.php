<?php

/* @var $this \yii\web\View */
/* @var $content string */

use app\widgets\Alert;
use yii\helpers\Html;
use yii\bootstrap\Nav;
use yii\bootstrap\NavBar;
use yii\widgets\Breadcrumbs;
use app\assets\AppAsset;

use webvimark\modules\UserManagement\components\GhostMenu;
use webvimark\modules\UserManagement\UserManagementModule;

AppAsset::register($this);
?>
<?php $this->beginPage() ?>
<!DOCTYPE html>
<html lang="<?= Yii::$app->language ?>">
<head>
    <meta charset="<?= Yii::$app->charset ?>">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <?= Html::csrfMetaTags() ?>
    <title><?= Html::encode($this->title) ?></title>
    <?php $this->head() ?>
</head>
<body>
<?php $this->beginBody() ?>

<div class="wrap">
    <?php
    NavBar::begin([
        'brandLabel' => Yii::$app->name,
        'brandUrl' => Yii::$app->homeUrl,
        'options' => [
            'class' => 'navbar-inverse navbar-fixed-top',
        ],
    ]);

    echo Nav::widget([
        'options' => ['class' => 'navbar-nav navbar-right'],
        'encodeLabels'=>false,
        'activateParents'=>true,
        'items' => [

            ['label' => 'Home', 'url' => ['/site/index']],

            [
                'label' => 'Publicações',
                'items'=>[

                    ['label'=>'Pub. de usuários', 'url'=>['/publicacaouser/index']],
                    ['label'=>'Pub. de pesquisadores', 'url'=>['/publicacaopesq/index']],
                    ['label'=>'Avaliar pub. de usuários', 'url'=>['/avaliacaopubuser/index']],
                    ['label'=>'Avaliar pub. de pesquisadores', 'url'=>['/avaliacaopubpesq/index']],
                    ['label' => 'Cadastrar pesquisador', 'url' => ['/pesquisador/index']],
                ],
            ],


            [
                'label' => 'Admin',
                'items'=>UserManagementModule::menuItems()
            ],

            Yii::$app->user->isGuest ?
                ['label' => 'Login', 'url' => ['/user-management/auth/login']] :
                ['label' => 'Sair (' . Yii::$app->user->identity->username . ')',
                    'url' => ['/user-management/auth/logout'],
                    'linkOptions' => ['data-method' => 'post']],

            /*  [
                                                                                                                                                                                                                  'label' => 'config',
                                                                                                                                                                                                                  'items'=>[
                                                                                                                                                                                                                  //  ['label'=>'Login', 'url'=>['/user-management/auth/login']],
                                                                                                                                                                                                                  //  ['label'=>'Logout', 'url'=>['/user-management/auth/logout']],
                                                                                                                                                                                                                  //    ['label'=>'Mude sua senha', 'url'=>['/user-management/auth/change-own-password']],
                                                                                                                                                                                                                  //    ['label'=>'Password recovery', 'url'=>['/user-management/auth/password-recovery']],
                                                                                                                                                                                                                  //    ['label' => 'Home', 'url' => ['/site/index']],
                                                                                                                                                                                                                  ],
                                                                                                                                                                                                          ],

                                                                                                                                                                                                                  //  ['label'=>'Login', 'url'=>['/user-management/auth/login']],
                                                                                                                                                                                                        //  ['label'=>'Logout', 'url'=>['/user-management/auth/logout']],


                                                                                                                                                                                                        */

        ],
    ]);



  /*  echo Nav::widget([
        'options' => ['class' => 'navbar-nav navbar-right'],
        'items' => [
            ['label' => 'Início', 'url' => ['/site/index']],
            ['label' => 'Contato', 'url' => ['/site/contact']],
        ],
    ]); */
    NavBar::end();
    ?>

    <div class="container">
        <?= Breadcrumbs::widget([
            'links' => isset($this->params['breadcrumbs']) ? $this->params['breadcrumbs'] : [],
        ]) ?>
        <?= Alert::widget() ?>
        <?= $content ?>
    </div>
</div>

<footer class="footer">
    <div class="container">
        <p class="pull-left">&copy; My Company <?= date('Y') ?></p>

        <p class="pull-right"><?= Yii::powered() ?></p>
    </div>
</footer>

<?php $this->endBody() ?>
</body>
</html>
<?php $this->endPage() ?>
