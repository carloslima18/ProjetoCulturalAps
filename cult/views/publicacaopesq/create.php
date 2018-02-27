<?php

use yii\helpers\Html;


/* @var $this yii\web\View */
/* @var $model app\models\Publicacaopesq */

$this->title = 'Adicionar pub. pesquisador';
$this->params['breadcrumbs'][] = ['label' => 'Pub. pesquisador', 'url' => ['index']];
$this->params['breadcrumbs'][] = $this->title;
?>
<div class="publicacaopesq-create">

    <h1><?= Html::encode($this->title) ?></h1>

    <?= $this->render('_form', [
        'model' => $model,
    ]) ?>

</div>
