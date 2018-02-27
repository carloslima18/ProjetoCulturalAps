<?php

use yii\helpers\Html;

/* @var $this yii\web\View */
/* @var $model app\models\Avaliacaopubuser */

$this->title = 'Alterar avl. pub. user: {nameAttribute}';
$this->params['breadcrumbs'][] = ['label' => 'Avaliar pub. user', 'url' => ['index']];
$this->params['breadcrumbs'][] = ['label' => $model->pubuser->nome, 'url' => ['view', 'id' => $model->pubuser->nome]];
$this->params['breadcrumbs'][] = 'Alterar';
?>
<div class="avaliacaopubuser-update">

    <h1><?= Html::encode($this->title) ?></h1>

    <?= $this->render('_form', [
        'model' => $model,
    ]) ?>

</div>
